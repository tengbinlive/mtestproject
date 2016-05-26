# animation_seachview

[download  apk](http://fir.im/mu6c)


![download](https://github.com/tengbinlive/animation_seachview/blob/master/images/download.png)


![](https://github.com/tengbinlive/animation_seachview/blob/master/images/demo.gif) 

#### gradle

    dependencies {

          compile 'com.bin:animationseachview:1.0.2'

    }

#### maven

    <dependency>
      <groupId>com.bin</groupId>
      <artifactId>animationseachview</artifactId>
      <version>1.0.2</version>
      <type>pom</type>
    </dependency>

#### xml

    <com.bin.AnimationSearchView
            android:id="@+id/searchView"
            android:layout_width="match_parent"
            android:layout_margin="@dimen/search_margin"
            android:layout_height="match_parent" />


#### 如果需要监听输入框内容

    searchView.addTextChangedListener(new TextWatcherAdapter(){
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    super.onTextChanged(s, start, before, count);
                }
            });

#### 如果需要返回键关闭动画

    @Override
        public void onBackPressed() {
            if (searchView.isAnimationOpen()) {
                searchView.closeAnimation();
                return;
            }
            super.onBackPressed();
        }