# file: features/chat/consumer-rules.pro

# Quy tắc cho consumer ProGuard/R8
# Các quy tắc này được áp dụng cho code của module này khi nó được đóng gói vào ứng dụng cuối cùng.

# Giữ lại tất cả các lớp R và các lớp con R$* (R$anim, R$id, R$menu, v.v.)
# Điều này là rất quan trọng để đảm bảo R8 không loại bỏ các tham chiếu đến tài nguyên.
-keep class **.R
-keep class **.R$* { *; }

# Quy tắc cụ thể cho module 'code.common'
# Giữ lại các lớp R của package com.example.code.common.
# Điều này cần thiết nếu HomeChatActivity (trong module chat) tham chiếu trực tiếp
# các R classes từ code.common.
-keep class com.example.code.common.R$* { *; }

# Giữ lại các Activity, Fragment, View, v.v. nếu chúng được sử dụng trong module chat
# và có thể được khởi tạo bằng Reflection hoặc bởi hệ thống Android.
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Giữ lại các constructors cần thiết cho các custom Views
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Các quy tắc Hilt cho module chat (nếu chat sử dụng Hilt)
# Các quy tắc này rất quan trọng để đảm bảo Hilt có thể hoạt động đúng cách sau khi tối ưu hóa.
-keep class dagger.hilt.android.internal.managers.ActivityComponentManager
-keep class dagger.hilt.android.internal.managers.FragmentComponentManager
-keep class dagger.hilt.android.internal.managers.ServiceComponentManager
-keep class dagger.hilt.android.internal.managers.ViewComponentManager
-keep class dagger.hilt.android.internal.managers.ApplicationComponentManager
-keep class dagger.hilt.internal.aggregatedroot.codegen.* { *; }

# Giữ lại các interface builder của Hilt components
-keep class * implements dagger.hilt.android.internal.builders.ActivityComponentBuilder
-keep class * implements dagger.hilt.android.internal.builders.FragmentComponentBuilder
-keep class * implements dagger.hilt.android.internal.builders.ServiceComponentBuilder
-keep class * implements dagger.hilt.android.internal.builders.ViewComponentBuilder
# Giữ lại các component interfaces của Hilt
-keep class * implements dagger.hilt.android.components.ActivityComponent
-keep class * implements dagger.hilt.android.components.FragmentComponent
-keep class * implements dagger.hilt.android.components.ServiceComponent
-keep class * implements dagger.hilt.android.components.ViewComponent
-keep class * implements dagger.hilt.android.components.ActivityRetainedComponent
-keep class * implements dagger.hilt.android.components.ViewModelComponent
-keep class * implements dagger.hilt.android.components.ViewWithFragmentComponent

# Các quy tắc khác mà Hilt có thể yêu cầu (tham khảo tài liệu Hilt)
# Đôi khi các quy tắc cho @HiltViewModel hoặc @Inject có thể cần được tinh chỉnh thêm tùy theo cách bạn sử dụng.
# Nếu bạn vẫn gặp lỗi sau khi thêm các quy tắc này, hãy kiểm tra lại log chi tiết của R8
# hoặc tài liệu của Hilt về ProGuard/R8.