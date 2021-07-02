# ユーザを新規作成できる
tags: cleanInsertBeforeScenario
* URL"/users"に対してPOSTリクエスト<file:httpRequests/users/register/register.json>を実行する
* HTTPステータスコード"201"が返された

## レスポンスのJSONからユーザ情報を得られる
* レスポンスのJSONのパス"$.id"の値がUUID形式である
* レスポンスのJSONのパス"$.registeredAt"の値が過去"10"秒以内の日時データである
* レスポンスのJSONのパス"$.name"の値が"takehara"である
* レスポンスのJSONのパス"$.mailAddress"の値が"takehara@example.com"である
* レスポンスのJSONのパス"$.loginId"の値が"takehara@example.com"である

## DBのusersテーブルに新規レコードが存在する
* レスポンスのJSONのパス"$.id"の値が主キーであるレコードがusersテーブルに格納された
* DBのusersテーブルの列registered_atに登録日時を登録できた

## DBのuser_authsテーブルに新規レコードが存在する
* レスポンスのJSONのパス"$.id"の値が主キーであるレコードがuser_authsテーブルに格納された
* DBのuser_authsテーブルの列"login_id"に値"takehara@example.com"を登録できた
* DBのuser_authsテーブルの列"login_password"に値"password"をハッシュ化したものを登録できた

## DBのuser_profilesテーブルに新規レコードが存在する
* レスポンスのJSONのパス"$.id"の値が主キーであるレコードがuser_profilesテーブルに格納された
* DBのuser_profilesテーブルの列"name"に値"takehara"を登録できた
* DBのuser_profilesテーブルの列"mail_address"に値"takehara@example.com"を登録できた
