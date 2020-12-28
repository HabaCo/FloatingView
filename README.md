# FloatingView
A simple util to generate floating view with default style easily or custom view style easily.
> 一個可使用預設風格直接產生懸浮視窗；或是自訂內容的工具集

## Gradle

Bintray

    implementation 'github.habaco.view:view-floating:1.0.0'

Jitpack
    
    implementation 'com.github.HabaCo:FloatingView:1.0.0'
    
### Check canDrawOverlays
 FloatingUtil provides a simple util with compat function to check canDrawOverlay
 > FloatingUtil 提供一個快速的 compat function 用以確認是否能在上層繪製
 ```kotlin
     FloatingUtil.needToGrantOverlaysPermission(context, callbackIfNeed)
 ```
 
### Navigate to Settings
FloatingUtil provides a simple util can navigate user go to settings with "Allow display over other apps"
> FloatingUtil 提供一個快速的 compat function 用以將 user 導向 "開啟上層繪製" 的設定頁面
```kotlin
  FloatingUtil.startSetting(context)
```
 
### FloatingView Builder
FloatingView design with Builder
> FloatingView 提供一個 Builder
```kotlin
  val builder = FloatingView.Builder(context)  
```
 
You can set attributes with this builder, and you can simply give few data to use default style.
> 你可以透過 Builder 設定屬性，並且僅需要些許的資料即可套用預設風格
```kotlin
  builder.title(title)
      .message(message)
      .icon(drawable)
```
 
or custom yourself
> 或是提供自定義的 View
```kotlin
  builder.contentView(customView)
```

and set some callback you want
> 再加上一些需要使用的 callback
```kotlin
  builder.onShow { }
      .onHide { }
      .onClick { }
```

finally build it and get FloatingView
> 最後建立取得 FloatingView 的實體
```kotlin
val floatingView = builder.build()
```

that's all
> 結束

<img src="preview/preview1.gif" title="with click on view" alt="drawing" width="250"/> <img src="preview/preview2.gif" title="without click on view" alt="drawing" width="250"/> <img src="preview/preview3.gif" title="without click on view" alt="drawing" width="250"/>

## Notice
Beacuase __floating-view__ is managed by WindowManager, it can exisit even when app process is killed, In this case we will lose the reference with __floating-view__ eventually, so you need to hold the reference carefully, or you can add some control behavior to __floating-view__, such as ***remove self from WindowManager***.
> 因為 __lofating-view__ 是被 WindowManager 所管理，甚至可以在 app 的程序關閉時還存在，在這個情況下我們最終將失去 __floating-view__ 的參照位置，所以需要小心地持有 __floating-view__ 的參照位置，或是加上一些行為給 __floating-view__ ，例如 ***從 WindowManager 移除自己***
