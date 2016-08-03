# animation_searchview

![](https://github.com/tengbinlive/mtestproject/blob/master/images/demo.gif) 

#### gradle

    dependencies {

          compile 'com.bin:animationsearchview:1.2.1'

    }

#### maven

    <dependency>
      <groupId>com.bin</groupId>
      <artifactId>animationsearchview</artifactId>
      <version>1.2.1</version>
      <type>pom</type>
    </dependency>

#### xml

    <RelativeLayout
        android:focusable="true"
        android:focusableInTouchMode="true">

        <com.bin.AnimationSearchView
            android:id="@+id/searchView"
            android:layout_width="match_parent"
            android:layout_margin="@dimen/search_margin"
            android:layout_height="match_parent" />
            
    </RelativeLayout>


#### listener

text changed

    searchView.addTextChangedListener();
            
enter
    
    searchView.addEditorActionListener();

animationChange

    searchView.setAnimationChange();

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

