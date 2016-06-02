# animation_seachview

![](https://github.com/tengbinlive/mtestproject/blob/master/images/demo.gif) 

#### gradle

    dependencies {

          compile 'com.bin:animationseachview:1.0.7'

    }

#### maven

    <dependency>
      <groupId>com.bin</groupId>
      <artifactId>animationseachview</artifactId>
      <version>1.0.6</version>
      <type>pom</type>
    </dependency>

#### xml

    <com.bin.AnimationSearchView
            android:id="@+id/searchView"
            android:layout_width="match_parent"
            android:layout_margin="@dimen/search_margin"
            android:layout_height="match_parent" />


#### 监听输入框内容

    searchView.addTextChangedListener(new TextWatcherAdapter(){
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    super.onTextChanged(s, start, before, count);
                }
            });

#### 返回键关闭动画

    @Override
        public void onBackPressed() {
            if (searchView.isAnimationOpen()) {
                searchView.closeAnimation();
                return;
            }
            super.onBackPressed();
        }
        
#### 修改search背景 

    添加 search_content_bg.xml
    
    <?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android" >
    
        <solid android:color="#FFFFFF" />
        <corners
            android:bottomLeftRadius="8dp"
            android:bottomRightRadius="8dp"
            android:topLeftRadius="8dp"
            android:topRightRadius="8dp" />
    
    </shape>
    
    或是
    
    searchView.setSearchContentBackgroundColor(R.drawable.bg);

