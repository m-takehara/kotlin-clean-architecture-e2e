# ユーザ情報が競合する場合はユーザを新規作成できない

## 既存のユーザとメールアドレスが重複する場合はユーザ作成ができない
tags: cleanInsertBeforeScenario
* 既存のユーザがメールアドレス"yamada@example.com"を使っているときに、同じメールアドレスを使ってユーザ新規作成のリクエストを送る
* HTTPステータスコード"400"が返された
* レスポンスのJSONのパス"$.error"の値が"Email address is already used: yamada@example.com"である
* DBのテーブル"users,user_auths,user_profiles"が初期状態"before/scenario/database"から変化していない
