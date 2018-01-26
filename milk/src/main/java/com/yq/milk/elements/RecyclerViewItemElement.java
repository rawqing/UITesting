package com.yq.milk.elements;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.trubuzz.trubuzz.utils.Judge.isVisible;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by king on 16/12/7.
 */

public class RecyclerViewItemElement implements Element<Matcher<View>> {
    private static final String TAG = "jcd_" + RecyclerViewItemElement.class.getSimpleName();

    private final Matcher<View> recyclerMatcher;
    private int position = -1;
    private Matcher<View> findMatcher;

    public RecyclerViewItemElement(Matcher<View> recyclerMatcher) {
        this.recyclerMatcher = recyclerMatcher;
    }
    public RecyclerViewItemElement(Element<Matcher<View>> recyclerMatcher) {
        this.recyclerMatcher = recyclerMatcher.way();
    }

    public RecyclerViewItemElement setPosition(int position) {
        this.position = position;
        return this;
    }

    public RecyclerViewItemElement setFindMatcher(Matcher<View> findMatcher) {
        this.findMatcher = findMatcher;
        return this;
    }

    @Override
    public String toString() {
        return "RecyclerViewItemElement{" +
                "recyclerMatcher=" + recyclerMatcher +
                ", position=" + position +
                ", findMatcher=" + findMatcher +
                '}';
    }

    public static ViewAction scrollToRecyclerPosition(final int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(RecyclerView.class), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "scroll RecyclerView to position: " + position;
            }

            @Override
            public void perform(UiController uiController, View view) {
                scrollToPosition((RecyclerView) view,position);
            }
        };
    }

    public static void scrollToPosition(RecyclerView recyclerView ,int position){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, 0);
    }

    /**
     * 匹配指定位置的view
     * @param position
     * @return
     */
    public Matcher<View> atPosition(final int position){
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with RecyclerView position : " + position);
            }

            @Override
            protected boolean matchesSafely(View view) {
                ViewParent viewParent = view.getParent();
                if (!(viewParent instanceof RecyclerView))    return false;
                if (!recyclerMatcher.matches(viewParent)) return false;

                RecyclerView recyclerView = (RecyclerView) viewParent;
                View childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;

                return view == childView;
            }
        };
    }

    public Matcher<View> atMatcher(final Matcher<View> matcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("at matcher .");
                matcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                if(! matcher.matches(view))     return false;
                ViewParent parent = view.getParent();
                while (parent != null) {
                    if((parent instanceof RecyclerView) &&  recyclerMatcher.matches(parent))
                        return true;
                    if(!(parent instanceof View))   return false;
                    view = (View) parent;
                    parent = view.getParent();
                }
                return false;
            }
        };
    }


    @Override
    public Matcher<View> way() {
        if(position != -1){
            return this.atPosition(position);
        }
        if(findMatcher != null){
            return this.atMatcher(findMatcher);
        }
        return null;
    }

    public static int getPosition(final ViewInteraction viewInteraction , final Matcher<View> viewMatcher){
        final int[] position = {-1};
        viewInteraction.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(RecyclerView.class), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "get RecyclerView`s position .";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                int itemCount = recyclerView.getAdapter().getItemCount();
                for (int i = 0; i < itemCount; i++) {
                    View itemView = recyclerView.findViewHolderForAdapterPosition(i).itemView;
                    if(viewMatcher.matches(itemView)){
                        position[0] = i;
                    }
                }
            }
        });
        return position[0];
    }


    /**
     * 获取 RecyclerView 在当前可见的最后一个child (可见度90)
     * @param viewInteraction 一个匹配了 RecyclerView 的 ViewInteraction
     * @return 最后一个child View.
     */
    public static View getVisibleView(final ViewInteraction viewInteraction){
        final View[] visibleView = {null};
        viewInteraction.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(RecyclerView.class), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "get RecyclerView`s children , if current is visible 90% ";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                int childCount = recyclerView.getChildCount();
                for (int i = childCount - 1; i >= 0; i--) {
                    View childAt = recyclerView.getChildAt(i);
                    if (isVisible(childAt, 90)) {
                        visibleView[0] = childAt;
                        Log.i(TAG, "getVisibleView: childCount =" + childCount + " ; at child : " + i);
                        return;
                    }
                }
            }
        });
        return visibleView[0];
    }


}
