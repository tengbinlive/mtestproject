# tabbarview

![](https://github.com/tengbinlive/mtestproject/blob/master/images/demo2.gif) 

#### gradle

    dependencies {

          compile 'com.bin:tabbarview:1.0.4'

    }

#### maven

    <dependency>
      <groupId>com.bin</groupId>
      <artifactId>tabbarview</artifactId>
      <version>1.0.4</version>
      <type>pom</type>
    </dependency>

#### xml

      <com.bin.tabbarview.TabBarView
            android:id="@+id/view_tabbar"
            android:layout_width="match_parent"
            android:layout_height="@dimen/tabbar_height"
            android:layout_alignParentBottom="true"
            android:background="@color/tabbar_bg"
            android:gravity="center_vertical"
            android:padding="@dimen/content_gap_small" />



#### callback function

     viewPagerTab.setOnItemClickListener(new ECallOnClick() {
                @Override
                public void callOnClick(View view, Model item, int index) {
                    viewPager.setCurrentItem(index);
                }
            });

#### init item

     int whiteColor = getResources().getColor(R.color.white);
     int orangeColor = getResources().getColor(R.color.orange);
     
     Model item = new Model();
     item.setAnchor(4);                                 // 依赖对象(item 位于集合中的位置index+1,因为id为0无效);
     item.setTitle("title0x00");                        //title
     item.setTitleColorNormal(whiteColor);              //Unselected color title font
     item.setTitleColorPressed(orangeColor);            //selected color title font
     item.setIconNormal(R.mipmap.ic_test1_normal);      //Unselected icon
     item.setIconPressed(R.mipmap.ic_test1_pressed);    //selected icon
     item.setGravity(AnimationTabItem.GRAVITY_LEFT);    //item left | right
     items.add(item);
        

