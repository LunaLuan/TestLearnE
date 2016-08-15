package learnenglishhou.bluebirdaward.com.learne.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import learnenglishhou.bluebirdaward.com.learne.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DanhSachBaiHocActivityTest {

    @Rule
    public ActivityTestRule<DanhSachBaiHocActivity> mActivityTestRule = new ActivityTestRule<>(DanhSachBaiHocActivity.class);

    @Test
    public void danhSachBaiHocActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button), withText("Nghe thử"),
                        withParent(allOf(withId(R.id.activity_danh_sach_cau_hoi),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button), withText("Nghe thử"),
                        withParent(allOf(withId(R.id.activity_danh_sach_cau_hoi),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editText), withText("Name"),
                        withParent(allOf(withId(R.id.activity_danh_sach_cau_hoi),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editText), withText("Name"),
                        withParent(allOf(withId(R.id.activity_danh_sach_cau_hoi),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("ggvbjjjufff"));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editText), withText("ggvbjjjufff"),
                        withParent(allOf(withId(R.id.activity_danh_sach_cau_hoi),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editText), withText("ggvbjjjufff"),
                        withParent(allOf(withId(R.id.activity_danh_sach_cau_hoi),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editText), withText("ggvbjjjufff"),
                        withParent(allOf(withId(R.id.activity_danh_sach_cau_hoi),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("ggvbjjju"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editText), withText("ggvbjjju"),
                        withParent(allOf(withId(R.id.activity_danh_sach_cau_hoi),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

    }

}
