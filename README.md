# MyRecipe
A Android application to fetch recipes fromgiven API

### Installation
Clone this repository and import into Android Studio

```
git clone git@github.com:DSKarthikeyan/MyRecipe.git
```

### Functionality

1. List of available recipe's list from the provided public API
2. Handled loading state and error state.
3. Offline support. Once the data is fetched successfully from
   remote, it will stored locally.
4. Kotlin language
5. Dark and Light theme Support
6. MVVM project structure.

### Gradle configuration

To Fetch Server Side Data - Retrofit

```groovy
dependencies {
           implementation 'com.squareup.retrofit2:retrofit:2.6.1'
           implementation 'com.squareup.retrofit2:converter-gson:2.6.1'
           implementation "com.squareup.okhttp3:logging-interceptor:4.5.0"
}
```

For loading images Glide Library Used

```groovy
dependencies {
       implementation 'com.github.bumptech.glide:glide:4.11.0'
       kapt 'com.github.bumptech.glide:compiler:4.11.0'
}
```

For Local Room DB and Room DB coroutine support

```groovy
dependencies {
    def room_version = "2.2.5"
    implementation "android.arch.persistence.room:runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    // Kotlin Extensions and Coroutines support for Room
    implementation "androidx.room:room-ktx:2.2.5"
}
```


### Library

1. OKHTTP for efficient HTTP requests.
2. Retrofit to provide a typesafe layer.
3. Glide is another option for loading and caching images. It has support for animated GIFs, circular images and claims of better performance than Picasso, but also a bigger method count.
4. GSON is another popular choice and being a smaller library than Jackson, you might prefer it to avoid 65k methods limitation. Also, if you are using
5. ROOM to store Data Locally.

