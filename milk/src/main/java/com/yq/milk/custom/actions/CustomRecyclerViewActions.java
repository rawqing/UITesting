package com.yq.milk.custom.actions;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.util.HumanReadables;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.trubuzz.trubuzz.utils.DoIt;

import org.hamcrest.Matcher;

import java.util.Arrays;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.internal.util.Checks.checkNotNull;
import static com.trubuzz.trubuzz.constant.Env.VISIBILITY;
import static com.trubuzz.trubuzz.utils.Judge.isVisible;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by king on 16/12/8.
 */

public class CustomRecyclerViewActions {
    private static final String TAG = "jcd_" + CustomRecyclerViewActions.class.getSimpleName();

    public static ViewAction scrollToRecyclerPosition(int position) {
        return new ScrollToRecyclerPosition(position);
    }
    public static ViewAction atPositionAction(int position , ViewAction viewAction){
        return new AtPositionViewAction<>(position, viewAction);
    }
    public static ViewAction swipeToVisible(int p){
        return new SwipeToVisible(p);
    }
    public static ViewAction swipeUpToVisible(){
        return new SwipeUpToVisible();
    }
    public static ViewAction doActions(final ViewAction... viewActions){
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "do actions : "+ Arrays.toString(viewActions);
            }

            @Override
            public void perform(UiController uiController, View view) {
                checkNotNull(viewActions);
                for(ViewAction action : viewActions){
                    Log.i(TAG, "doActions perform do action : " + action.getDescription());
                    action.perform(uiController ,view);
                }
            }
        };
    }

    /************ ViewActions ***********/

    /**
     * 滚动到指定位置
     */
    public static final class ScrollToRecyclerPosition implements ViewAction {
        private int position;

        public ScrollToRecyclerPosition(int position) {
            this.position = position;
        }

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
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(position, 0);
//                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, position);

        }
    }

    /**
     * 操作指定位置的View
     * @param <VH>
     */
    private static final class AtPositionViewAction<VH extends RecyclerView.ViewHolder> implements
            ViewAction {
        private final int position;
        private final ViewAction viewAction;

        private AtPositionViewAction(int position, ViewAction viewAction) {
            this.position = position;
            this.viewAction = viewAction;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Matcher<View> getConstraints() {
            return allOf(isAssignableFrom(RecyclerView.class), isDisplayed());
        }

        @Override
        public String getDescription() {
            return "actionOnItemAtPosition performing ViewAction: " + viewAction.getDescription()
                    + " on item at position: " + position;
        }

        @Override
        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view;

            new ScrollToRecyclerPosition(position).perform(uiController, view);
            uiController.loopMainThreadUntilIdle();

            @SuppressWarnings("unchecked")
            VH viewHolderForPosition = (VH) recyclerView.findViewHolderForAdapterPosition(position);
            if (null == viewHolderForPosition) {
                throw new PerformException.Builder().withActionDescription(this.toString())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new IllegalStateException("No view holder at position: " + position))
                        .build();
            }

            View viewAtPosition = viewHolderForPosition.itemView;
            if (null == viewAtPosition) {
                throw new PerformException.Builder().withActionDescription(this.toString())
                        .withViewDescription(HumanReadables.describe(viewAtPosition))
                        .withCause(new IllegalStateException("No view at position: " + position)).build();
            }
            for (int i = 0; i < 5; i++) {
                if (isVisible(viewAtPosition, VISIBILITY)) {
                    break;
                } else {
                    new SwipeToVisible(position).perform(uiController, view);
                    Log.d(TAG, "AtPositionViewAction perform: not visible at position " + position + " .");
                }
            }
            viewAction.perform(uiController, viewAtPosition);
        }
    }

    /**
     * 滑动当前可见元素 , 使之前/后不可见的元素可见 .
     */
    public static final class SwipeToVisible implements ViewAction {

        private static final float EDGE_FUZZ_FACTOR = 0.083f;
        private int position;

        public SwipeToVisible(int position) {
            this.position = position;
        }

        @Override
        public Matcher<View> getConstraints() {
            return allOf(isAssignableFrom(RecyclerView.class), isDisplayed());
        }

        @Override
        public String getDescription() {
            return "swipe to view visible .";
        }

        @Override
        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view;
            int childCount = recyclerView.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                View childAt = recyclerView.getChildAt(i);

                if (isVisible(childAt, VISIBILITY)) {
                    int childAdapterPosition = recyclerView.getChildAdapterPosition(childAt);
                    if(position < childAdapterPosition){
                        new GeneralSwipeAction(Swipe.FAST,
                                DoIt.translate(GeneralLocation.TOP_CENTER, 0, EDGE_FUZZ_FACTOR),
                                GeneralLocation.BOTTOM_CENTER, Press.FINGER)
                                .perform(uiController ,childAt);
                        Log.d(TAG, "SwipeToVisible perform: swipe down .");
                    }else {
                        new GeneralSwipeAction(Swipe.FAST,
                                DoIt.translate(GeneralLocation.BOTTOM_CENTER, 0, -EDGE_FUZZ_FACTOR),
                                GeneralLocation.TOP_CENTER, Press.FINGER)
                                .perform(uiController, childAt);
                        Log.d(TAG, "SwipeToVisible perform: swipe up .");
                    }
                    return;
                }
            }
        }
    }

    /**
     * 向上滑动view至可见 , 由于不能提供position参考位置 , 故不能判断向上还是向下滑动.
     */
    public static final class SwipeUpToVisible implements ViewAction {

        private static final float EDGE_FUZZ_FACTOR = 0.083f;
        private ViewAction[] actions;

        public SwipeUpToVisible(ViewAction...actions) {
            this.actions = actions;
        }

        @Override
        public Matcher<View> getConstraints() {
            return allOf(isAssignableFrom(RecyclerView.class), isDisplayed());
        }

        @Override
        public String getDescription() {
            return "swipe up to visible .";
        }

        @Override
        public void perform(UiController uiController, View view) {
            int j = 1;
            RecyclerView recyclerView = (RecyclerView) view.getParent();
            do {
                if(isVisible(view, VISIBILITY)){
                    return;
                }else {
                    int childCount = recyclerView.getChildCount();
                    for (int i = childCount - 1; i >= 0; i--) {
                        View childAt = recyclerView.getChildAt(i);
                        if (isVisible(childAt, VISIBILITY)) {
                            new GeneralSwipeAction(Swipe.FAST,
                                    DoIt.translate(GeneralLocation.BOTTOM_CENTER, 0, -EDGE_FUZZ_FACTOR),
                                    GeneralLocation.TOP_CENTER, Press.FINGER)
                                    .perform(uiController, childAt);
                            break;
                        }
                    }
                }
                j++;
            }while (j <=5);

        }
    }
}
