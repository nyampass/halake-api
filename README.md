# halake-api

HaLake API サーバ

## インストール

TBD

## 実行方法

TBD

## HaLake API

コワーキングスベースHaLakeでは、HaLakeの種々の環境情報をいろいろなアプリケーションから利用できるようAPIを提供しています。
現在提供している環境情報は以下のとおりです:

- 温度
- 湿度
- 混雑度

各環境情報を取得するAPIの詳細をそれぞれ以下に説明します。

### 温度取得
現在のHaLakeの温度(摂氏)を取得します。

#### リクエスト
- HTTPメソッド: GET
- リソースURL: http://api.example.com/1.0/temperature

#### レスポンス

    // 成功時
        {"status":"ok",
             "temperature":<現在の温度(摂氏)を表す32bit以下の浮動小数点数>}

    // 失敗時
        {"status":"error",
             "message":<エラーメッセージ文字列>}

#### 例
    $ curl http://api.example.com/1.0/temperature


### 湿度取得
現在のHaLakeの湿度(%)を取得します。

#### リクエスト
- HTTPメソッド: GET
- リソースURL: http://api.example.com/1.0/humidity

#### レスポンス

    // 成功時
        {"status":"ok",
             "humidity":<現在の湿度(%)を表す32bit以下の浮動小数点数>}

    // 失敗時
        {"status":"error",
             "message":<エラーメッセージ文字列>}

#### 例
    $ curl http://api.example.com/1.0/humidity


### 混雑度取得
現在のHaLakeの混雑度を取得します。
混雑度は全体の座席数に対する使用されている座席数の割合に応じて、以下の3段階で表されます:

|混雑度|                                              |
|-----|----------------------------------------------|
|xx   |混雑度低。xx%程度の座席が使用されていることを示します。|
|50   |混雑度中。50%程度の座席が使用されていることを示します。|
|xx   |混雑度高。xx%程度の座席が使用されていることを示します。|

#### リクエスト
- HTTPメソッド: GET
- リソースURL: http://api.example.com/1.0/congestion

#### レスポンス

    // 成功時
        {"status":"ok",
             "congestion":<混雑度>}

    // 失敗時
        {"status":"error",
             "message":<エラーメッセージ文字列>}

#### 例
    $ curl http://api.example.com/1.0/congestion


## ライセンス

Copyright © 2015 Nyampass Co. Ltd.
