# animation_searchview

![](https://github.com/tengbinlive/mtestproject/blob/master/images/demo.gif) 

#### gradle

    dependencies {

          compile 'com.bin:animationsearchview:1.1.0'

    }

#### maven

    <dependency>
      <groupId>com.bin</groupId>
      <artifactId>animationsearchview</artifactId>
      <version>1.1.0</version>
      <type>pom</type>
    </dependency>

#### xml

    <com.bin.AnimationSearchView
            android:id="@+id/searchView"
            android:layout_width="match_parent"
            android:layout_margin="@dimen/search_margin"
            android:layout_height="match_parent" />


#### text changed listener

    searchView.addTextChangedListener(new TextWatcherAdapter(){
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    super.onTextChanged(s, start, before, count);
                }
            });

#### close animation

    @Override
        public void onBackPressed() {
            if (searchView.isAnimationOpen()) {
                searchView.closeAnimation();
                return;
            }
            super.onBackPressed();
        }
        
#### change background search

    add search_content_bg.xml
    
    <?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android" >
    
        <solid android:color="#FFFFFF" />
        <corners
            android:bottomLeftRadius="8dp"
            android:bottomRightRadius="8dp"
            android:topLeftRadius="8dp"
            android:topRightRadius="8dp" />
    
    </shape>
    
    or
    
    searchView.setSearchContentBackgroundColor(R.drawable.bg);

