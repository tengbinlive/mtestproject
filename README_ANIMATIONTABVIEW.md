# animation_tabview

![](https://github.com/tengbinlive/mtestproject/blob/master/images/demo1.gif) 

#### gradle

    dependencies {

          compile 'com.bin:animationtabview:1.0.2'

    }

#### maven

    <dependency>
      <groupId>com.bin</groupId>
      <artifactId>animationtabview</artifactId>
      <version>1.0.2</version>
      <type>pom</type>
    </dependency>

#### xml

    <com.bin.animationtabview.AnimationTabView
            android:id="@+id/tab_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:layout_margin="@dimen/margin_10"
            android:background="@drawable/bg_round_pink"
            android:padding="@dimen/padding_5" />


#### callback function

    tabView.setOnItemClickListener(new ECallOnClick() {
                @Override
                public void callOnClick(View view, AnimationTabItem item, int index) {
                    CommonUtil.showToast(item.getTitle());
                }
            });

#### init item

     int whiteColor = getResources().getColor(R.color.white);
     int orangeColor = getResources().getColor(R.color.orange);
     
     AnimationTabItem item = new AnimationTabItem();
     item.setTitle("title0x00");                        //title
     item.setTitleColorNormal(whiteColor);              //Unselected color title font
     item.setTitleColorPressed(orangeColor);            //selected color title font
     item.setIconNormal(R.mipmap.ic_test1_normal);      //Unselected icon
     item.setIconPressed(R.mipmap.ic_test1_pressed);    //selected icon
     item.setGravity(AnimationTabItem.GRAVITY_LEFT);    //item left | right
     items.add(item);
        

