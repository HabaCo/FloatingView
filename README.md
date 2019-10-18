# FloatingView
 A simple util to generate floating view with default style or simply custom function

Maven

    <dependency>
      <groupId>github.habaco.view</groupId>
      <artifactId>view-floating</artifactId>
      <version>1.0.0</version>
      <type>pom</type>
    </dependency>


Gradle

    implementation 'github.habaco.view:view-floating:1.0.0'

### Check canDrawOverlays
 FloatingUtil provides a simple util with compat function to check canDrawOverlay
 ```kotlin
     FloatingUtil.needToGrantOverlaysPermission(context, callbackIfNeed)
 ```
 
### Navigate to Settings
FloatingUtil provides a simple util can navigate user go to settings with "Allow display over other apps"
```kotlin
  FloatingUtil.startSetting(context)
```
 
### FloatingView Builder
FloatingView design with Builder
```kotlin
  val builder = FloatingView.Builder(this)  
```
 
You can set attributes with this builder, and you can simply give few data to use default style.
```kotlin
  builder.title(title)
      .message(message)
      .icon(drawable)
```
 
or custom yourself
```kotlin
  builder.contentView(customView)
```

and set some callback you want
```kotlin
  builder.onShow { }
      .onHide { }
      .onClick { }
```

finally build it and get FloatingView
```kotlin
val floatingView = builder.build()
```

that's all

<img src="preview/preview1.gif" title="with click on view" alt="drawing" width="250"/> <img src="preview/preview2.gif" title="without click on view" alt="drawing" width="250"/> <img src="preview/preview3.gif" title="without click on view" alt="drawing" width="250"/>
