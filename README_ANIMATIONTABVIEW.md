# animation_seachview

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


#### 回调事件

    tabView.setOnItemClickListener(new ECallOnClick() {
                @Override
                public void callOnClick(View view, AnimationTabItem item, int index) {
                    CommonUtil.showToast(item.getTitle());
                }
            });

#### 初始化item

     int whiteColor = getResources().getColor(R.color.white);
     int orangeColor = getResources().getColor(R.color.orange);
     
     AnimationTabItem item = new AnimationTabItem();
     item.setTitle("title0x00");                        //标题
     item.setTitleColorNormal(whiteColor);              //未选中标题字体颜色
     item.setTitleColorPressed(orangeColor);            //选中标题字体颜色
     item.setIconNormal(R.mipmap.ic_test1_normal);      //未选中icon
     item.setIconPressed(R.mipmap.ic_test1_pressed);    //选中icon
     item.setGravity(AnimationTabItem.GRAVITY_LEFT);    //item 左右位置
     items.add(item);
        

