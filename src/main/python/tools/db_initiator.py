import os
import re
import pandas as pd
import mysql.connector

def row_to_db_format(row):
    author_nm           = str(row['AUTHR_NM'])[:250] if pd.notna(row['AUTHR_NM']) else None
    pub_year            = str(re.sub(r'[^0-9]', '', row['PBLICTE_YEAR']))[:4] if pd.notna(row['PBLICTE_YEAR']) else None
    cl_smbl_no          = row['CL_SMBL_NO']
    book_smbl_no        = row['BOOK_SMBL_NO']
    title_nm            = row['TITLE_NM'][:250]
    isbn_thirteen_no    = row['ISBN_THIRTEEN_NO']

    if pub_year != None and 4 < len(pub_year):
        # print("PBLICTE_YEAR의 길이가 4 초과입니다.")
        # print("PBLICTE_YEAR: {pub_year}")
        return None
    else:
        pub_year = int(pub_year) if pub_year and pub_year != '' else None
    if pd.isna(cl_smbl_no) or 22 < len(cl_smbl_no): 
        # print("CL_SMBL_NO가 NaN이거나 길이가 22 초과입니다.")
        # print(f"CL_SMBL_NO: {str(cl_smbl_no)}")
        return None
    if pd.isna(book_smbl_no) or 22 < len(book_smbl_no): 
        # print("BOOK_SMBL_NO가 NaN이거나 길이가 22 초과입니다.")
        # print(f"BOOK_SMBL_NO: {str(book_smbl_no)}")
        return None
    if pd.isna(title_nm):
        # print("TITLE_NM가 NaN입니다.")
        # print(f"TITLE_NM: {str(title_nm)}")
        return None
    if pd.isna(isbn_thirteen_no) or not(13 == len(isbn_thirteen_no) or 10 == len(isbn_thirteen_no)):
        # print("ISBN_THIRTEEN_NO가 NaN이거나 길이가 10이나 13이 아닙니다.")
        # print(f"ISBN_THIRTEEN_NO: {str(isbn_thirteen_no)}")
        return None

    return (author_nm, pub_year, cl_smbl_no, book_smbl_no, title_nm, isbn_thirteen_no)

def load_csv_to_db():
    # MySQL 연결 설정
    db_config = {
        "host": "localhost",
        "user": "root",
        "password": "",
        "database": "doc_searching",
        "auth_plugin": "caching_sha2_password"
    }

    # MySQL 연결 생성
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor()

    # 테이블 생성 (이미 존재한다면 생략 가능)
    create_table_query = '''
        CREATE TABLE IF NOT EXISTS books (
            AUTHR_NM            varchar(250),
            PBLICTE_YEAR        int,
            CL_SMBL_NO          varchar(22)     not null,
            BOOK_SMBL_NO        varchar(22)     not null,
            TITLE_NM            varchar(250)    not null,
            ISBN_THIRTEEN_NO    varchar(13)     not null,
            primary             key(ISBN_THIRTEEN_NO)
        );
    '''
    cursor.execute(create_table_query)
    conn.commit()

    # 현재 스크립트 파일의 경로를 확인
    script_directory = os.path.dirname(os.path.abspath(__file__))

    # 상대 경로를 기준으로 디렉토리 경로 설정
    directory_path = os.path.join(script_directory, '..\data')

    # 디렉토리 내의 모든 파일 목록을 가져옴
    file_list = [file for file in os.listdir(directory_path) if file.endswith(".csv")]

    file_num = 1

    for file_path in file_list :
        file_path = os.path.join(directory_path, file_path)
        data = pd.read_csv(file_path, encoding='utf-8', dtype={
            'AUTHR_NM': str,
            'PBLICTE_YEAR': str,
            'CL_SMBL_NO': str,
            'BOOK_SMBL_NO': str,
            'TITLE_NM': str,
            'ISBN_THIRTEEN_NO': str
        })

        # DataFrame의 각 row를 데이터베이스에 삽입
        for index, row in data.iterrows():
            insert_query = '''
                INSERT IGNORE INTO books (AUTHR_NM, PBLICTE_YEAR, CL_SMBL_NO, BOOK_SMBL_NO, TITLE_NM, ISBN_THIRTEEN_NO)
                VALUES (%s, %s, %s, %s, %s, %s);
            '''
            if values := row_to_db_format(row):
                cursor.execute(insert_query, values)
                conn.commit()
        
        print(f"{file_num}. {file_path} : 완료되었습니다.")

    # 연결 종료
    cursor.close()
    conn.close()

    print("데이터가 MySQL 데이터베이스에 저장되었습니다.")

load_csv_to_db()
