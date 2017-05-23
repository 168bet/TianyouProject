-optimizationpasses 5
#-dontusemixedcaseclassnames
-repackageclasses
-useuniqueclassmembernames
-dontskipnonpubliclibraryclasses
-dontpreverify
-overloadaggressively
-verbose
-dontwarn
-keepattributes *Annotation*
-keepattributes JavascriptInterface
-keepattributes Signature

-keepattributes *Annotation*,*Exceptions*,Signature

-keep class **.R$* { *; }

#项目包
-keep class com.gamebegin.sdk.** { *; }

#----------------------
#-keepattributes SourceFile,LineNumberTable
#-keepnames class * { @butterknife.InjectView *;}
#-keepnames class * extends android.widget.** { *; }