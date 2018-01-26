package com.yq.milk.custom.matchers;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.yq.milk.constant.Env;
import com.yq.milk.utils.God;
import com.yq.milk.utils.Judge;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by king on 2016/9/23.
 */

public class CustomMatcher {

    private static final String TAG = "jcd_CustomMatcher";


    /**
     * 匹配指定对象
     * 使用方式 : assertThat("" , thisObject("1"));
     * @param obj
     * @return
     */
    public static Matcher<Object> thisObject(final Object obj) {
        return new TypeSafeMatcher<Object>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Object must be this : " + obj);
            }

            @Override
            protected boolean matchesSafely(Object item) {
                return item.equals(obj);
            }
        };
    }

    /**
     * 匹配指定文本
     * 使用方式 : assertThat("" , thisString("1"));
     *
     * @param str
     * @return
     */
    public static Matcher<String> thisString(final String str) {
        return new TypeSafeMatcher<String>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("text must be this : " + str);
            }

            @Override
            protected boolean matchesSafely(String item) {
                return item.equals(str);
            }
        };
    }


    /**
     * 匹配 toast
     *
     * @return
     */
    public static Matcher<Root> withToast() {
        return new TypeSafeMatcher<Root>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is toast");
            }

            @Override
            protected boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                    IBinder windowToken = root.getDecorView().getWindowToken();
                    IBinder appToken = root.getDecorView().getApplicationWindowToken();
                    if (windowToken == appToken) {
                        //means this window isn't contained by any other windows.
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * 使用activity来获取 Context 的方式匹配toast ( 官方使用方法 )
     * 优点 : 清晰的错误日志, 方便调试 ( 推荐使用 )
     *
     * @param toastStr
     * @param activities
     * @return
     */
    public static ViewInteraction getToast(String toastStr, ActivityTestRule... activities) {
        if (activities.length > 0 && activities[0] != null) {
            return onView(withText(toastStr))
                    .inRoot(withDecorView(not(is(activities[0].getActivity().getWindow().getDecorView()))));
        }
        return onView(withText(toastStr))
                .inRoot(withDecorView(not(is(God.getCurrentActivity(Env.instrumentation).getWindow().getDecorView()))));
    }

    /**
     * 通过孩子元素的位置匹配
     *
     * @param parentMatcher
     * @param position
     * @return
     */
    public static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /**
     * 通过祖先元素匹配 ( 看起来无法单独使用 )
     *
     * @param ancestorMatcher
     * @return
     */
    public static Matcher<View> withAncestor(final Matcher<View> ancestorMatcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with ancestor that  ");
                ancestorMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                ViewParent viewParent = view.getParent();
                while (true) {
                    if (ancestorMatcher.matches(viewParent)) {
                        return true;
                    } else {
                        viewParent = viewParent.getParent();
                        if (viewParent == null) return false;
                    }
                }
            }
        };
    }

    /**
     * 通过叔父元素的位置匹配 ( 位置相对于祖父而言 )
     *
     * @param uncleMatcher
     * @param position
     * @return
     */
    public static Matcher<View> withUnclePosition(final Matcher<View> uncleMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with uncle position that ");
                uncleMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                ViewGroup viewGroup = (ViewGroup) view.getParent().getParent();
                View uncleView = viewGroup.getChildAt(position);
                return uncleMatcher.matches(uncleView);
            }
        };
    }

    /**
     * 通过叔父(uncle)查找
     *
     * @param uncleMatcher
     * @return
     */
    public static Matcher<View> withUncle(final Matcher<View> uncleMatcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with uncle:");
                uncleMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                ViewParent grandParent = view.getParent().getParent();
                if (!(grandParent instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup grandParentGroup = (ViewGroup) grandParent;
                for (int i = 0; i < grandParentGroup.getChildCount(); i++) {
                    if (uncleMatcher.matches(grandParentGroup.getChildAt(i)))
                        return true;
                }
                return false;
            }
        };
    }

    /**
     * 使用 uncle view 相对于 自身父级元素的位置来匹配
     * 在父元素上一位置 为 -1 , 下一行则为 1 ,一次类推
     *
     * @param uncleMatcher
     * @param relativePosition
     * @return
     */
    public static Matcher<View> withUncleRelativePosition(final Matcher<View> uncleMatcher, final int relativePosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with uncle relative position that ");
                uncleMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                ViewGroup viewGroup = (ViewGroup) view.getParent().getParent();
                int selfIndex = 0;
                int count = viewGroup.getChildCount();
                for (; selfIndex < count; selfIndex++) {
                    View cv = viewGroup.getChildAt(selfIndex);
                    if (view.getParent().equals(cv)) {
                        Log.i(TAG, "matchesSafely: count = " + count);
                        Log.i(TAG, "matchesSafely:  view index = " + selfIndex);
                        break;
                    }
                }
                View uncleView = viewGroup.getChildAt(relativePosition + selfIndex);
                return uncleMatcher.matches(uncleView);
            }
        };
    }

    /**
     * 通过index定位
     *
     * @param index
     * @return
     */
    public static Matcher<View> withIndex(final int index) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with index: " + index);
            }

            @Override
            protected boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                if (!(parent instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup parentGroup = (ViewGroup) parent;
                return view.equals(parentGroup.getChildAt(index));
            }
        };
    }

    /**
     * 通过 view 来匹配
     * @param view
     * @return
     */
    public static Matcher<View> withView(final View view) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return view.equals(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with view : " + view);
            }
        };
    }

    /**
     * 通过堂兄妹匹配
     * @param cousinMatcher
     * @return
     */
    public static Matcher withCousin(final Matcher<View> cousinMatcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with cousin:");
                cousinMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                ViewParent grandParent = view.getParent().getParent();
                if (!(grandParent instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup grandParentGroup = (ViewGroup) grandParent;   //get 祖父
                for (int i = 0; i < grandParentGroup.getChildCount(); i++) {
                    View uncle = grandParentGroup.getChildAt(i);
                    if (!(uncle instanceof ViewGroup))
                        continue;

                    ViewGroup uncleGroup = (ViewGroup) uncle;       //get uncle
                    for (int j = 0; j < uncleGroup.getChildCount(); j++) {
                        View currentView = uncleGroup.getChildAt(j);
                        if (cousinMatcher.matches(currentView) && !view.equals(currentView))
                            return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * 检查包含指定字符串 ,
     * 会将原字串 和 比较字符串 中的多个空格替换成一个 , 再和指定字串比较
     * @param str
     * @return
     */
    public static Matcher<String> singleSpaceContains(final String str) {
        return new TypeSafeMatcher<String>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("text must contains this : " + str);
            }

            @Override
            protected boolean matchesSafely(String item) {
                return item.replaceAll("\\x20+", " ").contains(str.replaceAll("\\x20+", " "));
            }
        };
    }


    /**
     * 基于指定的兄弟姐妹匹配, 不包含自身.
     * {@link android.support.test.espresso.matcher.ViewMatchers#hasSibling } 则是包含自身的实现
     *
     * @param siblingMatcher
     * @return
     */
    public static Matcher<View> hasSiblingNoSelf(final Matcher<View> siblingMatcher) {
        checkNotNull(siblingMatcher);
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("has sibling: ");
                siblingMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                if (!(parent instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup parentGroup = (ViewGroup) parent;
                for (int i = 0; i < parentGroup.getChildCount(); i++) {
                    View siblingView = parentGroup.getChildAt(i);
                    if (siblingMatcher.matches(siblingView) && !siblingView.equals(view)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * 匹配一个ImageView 是有图像的
     *
     * @return
     */
    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }

    /**
     * 匹配任何元素
     * @return
     */
    public static Matcher<View> withAny(){
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with any .");
            }

            @Override
            protected boolean matchesSafely(View view) {
                return true;
            }
        };
    }

    /**
     * view 有至少 minChild 个孩子
     * @param minChild 必须大于等于 1 .
     * @return
     */
    public static Matcher<View> hasMoreChildren(final int minChild){
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                Log.i(TAG, "hasMoreChildren: view : " + view.toString() + " ,has " + childCount + " children .");
                return minChild <= childCount;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has more children .");
            }
        };
    }
    public static Matcher<View> hasMoreChildren() {
        return hasMoreChildren(1);
    }

    /**
     * 匹配最大孩子个数
     * @param max
     * @return
     */
    public static Matcher<View> hasMaxChildren(final int max){
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("has max %s children.", max));
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                Log.i(TAG, "hasMoreChildren: view : " + view.toString() + " ,has " + childCount + " children .");
                return max >= childCount;
            }
        };
    }

    /**
     * 匹配刚好孩子的个数
     * @param count
     * @return
     */
    public static Matcher<View> hasChildrenCount(final int count){
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("has %s children.", count));
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                Log.i(TAG, "hasMoreChildren: view : " + view.toString() + " ,has " + childCount + " children .");
                return count == childCount;
            }
        };
    }

    /**
     * 判断输入框是否为password
     *
     * @return
     */
    public static Matcher<View> isPassword() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is password .");
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof EditText))
                    return false;

                int inputType = ((EditText) view).getInputType();
                return inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) ||     //可预览的密码输入结构 即(129)
                        inputType == InputType.TYPE_NUMBER_VARIATION_PASSWORD ||
                        inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                        inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                        inputType == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
            }
        };
    }

    private static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * 使用正则表达式匹配Text
     * @param regexText
     * @return
     */
    public static Matcher<View> withRegexText(final String regexText) {
        checkNotNull(regexText);
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with regex text: " + regexText);
            }

            @Override
            protected boolean matchesSafely(TextView textView) {
                String text = textView.getText().toString();
                // 编译正则
                Pattern pattern = Pattern.compile(regexText);
                // 创建matcher对象
                java.util.regex.Matcher m = pattern.matcher(text);
                return m.matches();
            }
        };
    }

    /**
     * 使用通配符匹配Text
     * 目前限定通配符为 * , ? 两种
     * @param wildcardText
     * @return
     */
    public static Matcher<View> withWildcardText(final String wildcardText){
        checkNotNull(wildcardText);
        String regexText = wildcardText.replace("*",".*").replace("?",".?");;
        return withRegexText(regexText);
    }

    public static Matcher<View> withContainText(final String containText) {
        checkNotNull(containText);
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with contain text: " + containText);
            }

            @Override
            protected boolean matchesSafely(TextView textView) {
                String text = textView.getText().toString();

                return text.contains(containText);
            }
        };
    }

    public static Matcher<View> withVisible(final int areaPercentage){
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with visible : " + areaPercentage);
            }

            @Override
            protected boolean matchesSafely(View view) {
                return Judge.isVisible(view ,areaPercentage);
            }
        };
    }

    public static Matcher<View> NotMatched(final Matcher<View> matcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(View item) {
                if (matcher.matches(item)) {
                    return false;
                }
                return true;
            }
        };
    }
}
