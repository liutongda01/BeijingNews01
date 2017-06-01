package mobileplayer.beijingnews01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import utils.CacheUtils;

public class WelcomeActivity extends AppCompatActivity {
    private RelativeLayout activity_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        activity_welcome = (RelativeLayout)findViewById(R.id.activity_welcome);
        setAnimation();
    }

    private void setAnimation() {
        //三个动画：旋转动画，渐变动画，缩放动画
        RotateAnimation ra = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        //设置持续时间
        ra.setDuration(2000);
        //设置停留在旋转后的状态
        ra.setFillAfter(true);

        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5F);
        sa.setDuration(2000);
        sa.setFillAfter(true);
        //三个同时起作用用Animation
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(ra);
        set.addAnimation(sa);

        //开始播放动画
        activity_welcome.startAnimation(set);
        //监听动画播放完成
        set.setAnimationListener(new MyAnimationListener() );
    }

    private class MyAnimationListener implements Animation.AnimationListener {
        //当动画开始的时候回调
        @Override
        public void onAnimationStart(Animation animation) {
            boolean startMain = CacheUtils.getBoolean(WelcomeActivity.this,"start_main");
            Intent intent = null;
            if(startMain) {
                //进入主页面
                intent = new Intent(WelcomeActivity.this,MainActivity.class);
            }else {
                //进入引导页面
                intent = new Intent(WelcomeActivity.this,GuideActivity.class);

            }
            startActivity(intent);
            finish();
        }
        //当动画重复播放的时候回调
        @Override
        public void onAnimationEnd(Animation animation) {

        }
        //当动画结束的时候回调
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
