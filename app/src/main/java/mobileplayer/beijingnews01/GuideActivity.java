package mobileplayer.beijingnews01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import utils.CacheUtils;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.btn_start_main)
    Button btnStartMain;
    @InjectView(R.id.ll_group_point)
    LinearLayout llGroupPoint;
    @InjectView(R.id.iv_red_point)
    ImageView ivRedPoint;

    private int[] ids = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private int leftMagin;


    /**
     * 红点移动的原理
     * 红点移动的距离： 间距 = 手滑动的距离：屏幕宽 = 屏幕滑动的百分比
     * 红点移动的距离 = 间距 * 屏幕滑动的百分比
     * <p>
     * 间距 = 第一个点距离左边距离 - 第0个点距离左边的距离
     * <p>
     * 红点移动的坐标 = 起始坐标 + 红点移动距离
     */
    //自定义控件 一个控件生命周期主要的三个方法：测量 布局 绘制。
    // 测量的目的得到控件的高和宽 间距。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        //设置适配器
        viewpager.setAdapter(new MyPagerAdapter());
        //监听页面的改变
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        //根据多少个页面添加多少个灰色的点
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);//10是像素的高和宽
            if (i != 0) {
                //设置灰色点的间距
                params.leftMargin = 10;
            }
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.point_normal);
            //添加到线性布局
            llGroupPoint.addView(imageView);
        }
        //View生命周期：测量--布局--绘制  监听的目的得到宽和高
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
    }
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{
        //跟这里求间距是因为到了布局阶段了
        @Override
        public void onGlobalLayout() {
            //打印20两次变为一次 性能优化一点
            ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            //间距 = 第一个点距离左边距离 - 第0个点距离左边的距离
            leftMagin = llGroupPoint.getChildAt(1).getLeft() - llGroupPoint.getChildAt(0).getLeft();
            Log.e("TAG","leftMagin=="+leftMagin);
        }
    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当滑动了页面的时候回调
         *
         * @param position             当前滑动页面的位置
         * @param positionOffset       滑动的百分比
         * @param positionOffsetPixels 滑动的像素（像素）
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //红点移动的距离： 间距 = 手滑动的距离：屏幕宽 = 屏幕滑动的百分比
            int maginLeft = (int) (leftMagin * positionOffset);
            // 红点移动的坐标 = 起始坐标 + 红点移动距离
            maginLeft = position * leftMagin+(int) (leftMagin * positionOffset);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            params.leftMargin = maginLeft;
            ivRedPoint.setLayoutParams(params);

            Log.e("TAG","maginLeft=="+maginLeft+","+"positionOffset=="+positionOffset+",positionOffsetPixels=="+positionOffsetPixels);
        }

        //当某个页面被选中的时候回调
        @Override
        public void onPageSelected(int position) {
            if (position == ids.length - 1) {
                //显示按钮
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                //隐藏按钮
                btnStartMain.setVisibility(View.GONE);
            }
        }

        //当状态变化的时候回调
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setBackgroundResource(ids[position]);
            //添加到ViewPager
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //viewpage或是viewGroup最多创建两个页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @OnClick(R.id.btn_start_main)
    public void onViewClicked() {
        //1.保存参数,记录已经进入引导页面，下次就不进了
        CacheUtils.putBoolean(this,"start_main",true);
        //2进入主页面
        //Toast.makeText(this, "进入主页面", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
