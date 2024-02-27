package com.example.booksearching.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.booksearching.elasticsearch.analysis.HanguelJamoType;
import com.example.booksearching.elasticsearch.analysis.HangulJamoMorphTokenizer;
import com.example.booksearching.elasticsearch.model.Book;
import com.example.booksearching.elasticsearch.model.BookDocument;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationException;
import com.example.booksearching.spring.exception.ElasticsearchCommunicationExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class DataBaseIndexingApplication {
  public static List<Book> books;
  public static List<BookDocument> bookDocs;
  public static HangulJamoMorphTokenizer morphTokenizer;

  public static void main(String[] args) {
    morphTokenizer = HangulJamoMorphTokenizer.getInstance();
    executeBookPipeline();
    System.exit(0);
  }

  public static void executeBookPipeline() {
    try {
      readBookData();
      convertBookToBookDocument();
      createIndex();
      indexingDocs();
    } catch (Exception e) {
      log.error("Error in executeBookPipeLine: ", e);
    }
  }

  public static void readBookData() {
    final String url = "change me";
    final String username = "change me";
    final String password = "change me";

    try (Connection connection = DriverManager.getConnection(url, username, password)) {
      log.info("Database Connected!");
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
      books = new ArrayList<>();

      while (resultSet.next()) {
        Book book = Book.of(
                resultSet.getString("isbn"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getInt("publish_year"),
                resultSet.getInt("price")
        );
        books.add(book);
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Cannot connect the database!", e);
    }
  }

  public static void convertBookToBookDocument() {
    bookDocs = new ArrayList<>();
    BookDocument doc;

    log.info("Start converting...");
    long start = System.currentTimeMillis();
    for (Book book : books) {
      doc = BookDocument.of(
        book.getIsbnThirteenNo(),
        book.getTitleName(),
        morphTokenizer.tokenizer(book.getTitleName(), HanguelJamoType.CHOSUNG),
        morphTokenizer.tokenizer(book.getTitleName(), HanguelJamoType.JAMO),
        morphTokenizer.tokenizer(book.getTitleName(), HanguelJamoType.KORTOENG),
        book.getAuthorName(),
        book.getPublicationYear(),
        book.getPrice()
      );
      bookDocs.add(doc);
    }
    long end = System.currentTimeMillis();
    log.info("수행시간: " + (end - start) + " ms");

    log.info("Total Converted Documents : {}", bookDocs.size());
  }

  public static void createIndex() throws IOException {
    final String INDEX_NAME = "books";

    ElasticsearchClient esClient = createESClient(60L);
    for (IndicesRecord index : esClient.cat().indices().valueBody()) {
      Optional.ofNullable(index.index()).orElseThrow(() ->
              new ElasticsearchCommunicationException(ElasticsearchCommunicationExceptionType.ELASTICSEARCH_SEARCH_RESPONSE_EXCEPTION));
      if (index.index().equals(INDEX_NAME)) {
        log.info("{} index is already exist!", index.index());
        DeleteIndexResponse deleteIndexRes = esClient.indices().delete(builder -> builder.index(INDEX_NAME));
        log.info("{} index is deleted!", index.index());
        log.info("DeleteIndexResponse: {}", deleteIndexRes);
      }
    }

    CreateIndexResponse createIndexRes =
            esClient.indices().create(builder ->
                    builder.index(INDEX_NAME)
                            .withJson(new StringReader(
                            "{" +
                              "\"settings\":{" +
                                "\"index\":{" +
                                  "\"number_of_shards\":1," +
                                  "\"number_of_replicas\":0," +
                                  "\"max_ngram_diff\":30" +
                                "}," +
                                "\"analysis\":{" +
                                  "\"analyzer\":{" +
                                    "\"ngram-book\":{" +
                                      "\"type\":\"custom\"," +
                                      "\"tokenizer\":\"partial\"," +
                                      "\"filter\":[\"lowercase\"]" +
                                    "}," +
                                    "\"edge-book\":{" +
                                      "\"type\":\"custom\"," +
                                      "\"tokenizer\":\"edge\"," +
                                      "\"filter\":[\"lowercase\"]" +
                                    "}" +
                                  "}," +
                                  "\"tokenizer\":{" +
                                    "\"partial\":{" +
                                      "\"type\":\"ngram\"," +
                                      "\"min_gram\":2," +
                                      "\"max_gram\":30," +
                                      "\"token_chars\":[\"letter\",\"digit\"]" +
                                    "}," +
                                    "\"edge\":{" +
                                      "\"type\":\"edge_ngram\"," +
                                      "\"min_gram\":1," +
                                      "\"max_gram\":30," +
                                      "\"token_chars\":[\"letter\",\"digit\"]" +
                                    "}" +
                                  "}," +
                                  "\"normalizer\":{" +
                                    "\"normalizer-book\":{" +
                                      "\"type\":\"custom\"," +
                                      "\"filter\":[\"lowercase\"]" +
                                    "}" +
                                  "}" +
                                "}" +
                              "}," +
                              "\"mappings\":{" +
                                "\"_source\":{" +
                                  "\"excludes\":[\"title_chosung\",\"title_jamo\",\"title_engtokor\"]" +
                                "}," +
                                "\"properties\":{" +
                                  "\"isbn\":{" +
                                    "\"type\":\"keyword\"" +
                                  "}," +
                                  "\"title\":{" +
                                    "\"type\":\"keyword\"," +
                                    "\"normalizer\":\"normalizer-book\"," +
                                    "\"fields\":{" +
                                      "\"kor\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"nori\"" +
                                      "}," +
                                      "\"en\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"standard\"" +
                                      "}," +
                                      "\"edge\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"edge-book\"" +
                                      "}," +
                                      "\"partial\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"ngram-book\"" +
                                      "}" +
                                    "}" +
                                  "}," +
                                  "\"title_chosung\":{" +
                                    "\"type\":\"keyword\"," +
                                    "\"normalizer\":\"normalizer-book\"," +
                                    "\"fields\":{" +
                                      "\"edge\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"edge-book\"" +
                                      "}," +
                                      "\"partial\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"ngram-book\"" +
                                      "}" +
                                    "}" +
                                  "}," +
                                  "\"title_jamo\":{" +
                                    "\"type\":\"keyword\"," +
                                    "\"normalizer\":\"normalizer-book\"," +
                                    "\"fields\":{" +
                                      "\"edge\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"edge-book\"" +
                                      "}," +
                                      "\"partial\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"ngram-book\"" +
                                      "}" +
                                    "}" +
                                  "}," +
                                  "\"title_engtokor\":{" +
                                    "\"type\":\"keyword\"," +
                                    "\"normalizer\":\"normalizer-book\"," +
                                    "\"fields\":{" +
                                      "\"edge\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"edge-book\"" +
                                      "}," +
                                      "\"partial\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"ngram-book\"" +
                                      "}" +
                                    "}" +
                                  "}," +
                                  "\"author\":{" +
                                    "\"type\":\"keyword\"," +
                                    "\"normalizer\":\"normalizer-book\"," +
                                    "\"fields\":{" +
                                      "\"kor\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"nori\"" +
                                      "}," +
                                      "\"en\":{" +
                                        "\"type\":\"text\"," +
                                        "\"analyzer\":\"standard\"" +
                                      "}" +
                                    "}" +
                                  "}," +
                                  "\"published_year\":{" +
                                    "\"type\":\"date\"" +
                                  "}," +
                                  "\"price\":{" +
                                    "\"type\":\"double\"" +
                                  "}" +
                                "}" +
                              "}" +
                            "}"
            )));
    log.info("{} index is created!", createIndexRes.index());
  }

  // https://discuss.elastic.co/t/api-key-unable-to-find-apikey-with-id/322104
  // https://www.elastic.co/guide/en/kibana/current/api-keys.html
  // https://www.elastic.co/guide/en/elasticsearch/reference/current/security-api-create-api-key.html

  // https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/connecting.html
  // https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/8.11/_other_authentication_methods.html
  // https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/_encrypted_communication.html

  // https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/indexing-bulk.html
  public static void indexingDocs() {

    int totalSize = bookDocs.size();
    int batchSize = 10000;
    AtomicInteger completedDocumentCount = new AtomicInteger(0);

    ElasticsearchClient esClient = createESClient((30 * (long) Math.ceil(totalSize / (double) batchSize)));

    log.info("Start indexing...");
    long start = System.currentTimeMillis();

    try {
      IntStream.range(0, (totalSize + batchSize - 1) / batchSize)
              .mapToObj(i -> bookDocs.subList(i * batchSize, Math.min((i + 1) * batchSize, totalSize)))
              .forEach(subList -> {
                processBulkRequest(esClient, subList);
                int cdc = completedDocumentCount.addAndGet(subList.size());
                log.info(String.format("%.2f%% - %d/%d tasks completed!", 100.0 * cdc / (double)totalSize, cdc, totalSize));
              });
    } catch (Exception e) {
      log.error("Create or request Elasticsearch request error", e);
    }

    long end = System.currentTimeMillis();
    log.info("수행시간: " + (end - start) + " ms");
    log.info("Total Generated Documents : {}", bookDocs.size());
  }

  public static ElasticsearchClient createESClient(long connectionTimeToLive) {
    // URL and API key
    String host = "localhost";
    int port = 9200;
    String encodedApiKey = "change me";

    String fingerprint = "change me";

    SSLContext sslContext = TransportUtils.sslContextFromCaFingerprint(fingerprint);

    BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
    credsProv.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "change me"));

    // Create the low-level client
    RestClient restClient = RestClient
            .builder(new HttpHost(host, port, "https"))
            .setDefaultHeaders(new Header[]{
                    new BasicHeader("Authorization", "ApiKey " + encodedApiKey)
            })
            .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                    .setSSLContext(sslContext)
                    .setDefaultCredentialsProvider(credsProv)
                    .setConnectionTimeToLive(connectionTimeToLive, TimeUnit.SECONDS)
            )
            .build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

    // And create the API client
    return new ElasticsearchClient(transport);
  }

  public static void processBulkRequest(ElasticsearchClient esClient, List<BookDocument> bookDocs) {
    BulkRequest.Builder br = new BulkRequest.Builder();

    for (BookDocument bookDoc : bookDocs) {
      br.operations(op -> op
              .index(idx -> idx
                      .index("books")
                      .id(bookDoc.getIsbn_thirteen_no())
                      .document(bookDoc))
      );
    }

    BulkResponse result;
    try {
      result = esClient.bulk(br.build());

      // Log errors, if any
      if (result.errors()) {
        log.error("Bulk had errors");
        for (BulkResponseItem item : result.items()) {
          if (item.error() != null) {
            log.error(item.error().reason());
          }
        }
      }
    } catch (Exception e) {
      log.error("Elasticsearch java api error [bulk]: ", e);
    }
  }
}
