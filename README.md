1. Project Description
   
Dự án này tập trung vào việc tìm hiểu các khái niệm nền tảng về Widget trong Flutter thông qua việc xây dựng một thành phần giao diện tùy chỉnh (Custom Widget) cho trò chơi đoán chữ.

Mục tiêu: Hiểu cấu trúc của một StatelessWidget, cách sử dụng Constructor để truyền dữ liệu và kỹ thuật lồng ghép (composition) các widget cơ bản.

Kiến thức trọng tâm:

Sử dụng Container và BoxDecoration để tạo kiểu dáng (borders, colors, shape).

Áp dụng switch expression trong Dart để thay đổi màu sắc widget dựa trên trạng thái dữ liệu.

Quản lý bố cục đơn giản với Center và Text.

2. Screenshots

![img.png](img.png)

3. Key Learnings 

Trong quá trình thực hiện bài tập này, tôi đã nắm vững các quy tắc sau từ Flutter Architecture:


Stateless over Stateful: Ưu tiên sử dụng StatelessWidget khi UI không cần thay đổi trạng thái nội tại sau khi dựng.


Extract UI parts: Chia nhỏ giao diện thành các widget riêng biệt (Tile) để mã nguồn sạch sẽ và dễ tái sử dụng.


Naming Conventions: Tuân thủ cách đặt tên PascalCase cho Classes và camelCase cho các biến/tham số trong Dart.


Composition: Xây dựng giao diện bằng cách kết hợp nhiều widget nhỏ lại với nhau thay vì viết một widget quá lớn.