# RxMediaLoader

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Load local media with RxJava for Android

# Requirement

- Android 4.0+ (API 14+)

# Usage

## Folder

Folder class represents a directory such as Camera.

- id(String): Internal identity
- name(String): Directory name for display
- cover(Media): Cover media in this folder
- medias(List<Media>): All medias in this folder

## Media

Media class represents a photo or video.

- id(long): Internal identity
- type(Type): Type of this media (Type.Photo or Type.Video)
- uri(Uri): Uri of this media

## Load all medias

```java
RxMediaLoader.medias(this, getSupportLoaderManager())
        .subscribe(new Subscriber<List<Folder>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(List<Folder> folders) {
                // Write your awesome code!
            }
        });
```

## Load only photos

```java
RxMediaLoader.photos(this, getSupportLoaderManager())
        .subscribe(new Subscriber<List<Folder>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(List<Folder> folders) {
                // Write your awesome code!
            }
        });
```

## Load only videos

```java
RxMediaLoader.videos(this, getSupportLoaderManager())
        .subscribe(new Subscriber<List<Folder>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(List<Folder> folders) {
                // Write your awesome code!
            }
        });
```

# License

```
Copyright 2016 yuyakaido

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
