# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}
-keep class **.R
-keep class **.R$* { *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.DialogFragment
-keep public class * extends android.view.View
-keep public class * extends android.webkit.WebView
-keep public class * extends android.os.AsyncTask
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class dagger.hilt.android.internal.managers.ActivityComponentManager
-keep class dagger.hilt.android.internal.managers.FragmentComponentManager
-keep class dagger.hilt.android.internal.managers.ServiceComponentManager
-keep class dagger.hilt.android.internal.managers.ViewComponentManager
-keep class dagger.hilt.android.internal.managers.ApplicationComponentManager
-keep class dagger.hilt.android.internal.modules.* { *; }
-keep class dagger.hilt.internal.aggregatedroot.codegen.* { *; }
-keep class * implements dagger.hilt.android.internal.builders.ActivityComponentBuilder
-keep class * implements dagger.hilt.android.internal.builders.FragmentComponentBuilder
-keep class * implements dagger.hilt.android.internal.builders.ServiceComponentBuilder
-keep class * implements dagger.hilt.android.internal.builders.ViewComponentBuilder
-keep class * implements dagger.hilt.android.components.ActivityComponent
-keep class * implements dagger.hilt.android.components.FragmentComponent
-keep class * implements dagger.hilt.android.components.ServiceComponent
-keep class * implements dagger.hilt.android.components.ViewComponent
-keep class * implements dagger.hilt.android.components.ActivityRetainedComponent
-keep class * implements dagger.hilt.android.components.ViewModelComponent
-keep class * implements dagger.hilt.android.components.ViewWithFragmentComponent

