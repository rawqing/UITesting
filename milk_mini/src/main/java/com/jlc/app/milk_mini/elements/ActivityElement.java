package com.jlc.app.milk_mini.elements;

import android.view.View;

import com.jlc.app.milk_mini.shell.AdViewInteraction;

import org.hamcrest.Matcher;

import java.util.Arrays;

import static com.jlc.app.milk_mini.shell.Park.given;
import static com.jlc.app.milk_mini.utils.DoIt.notEmpty;

/**
 * Created by king on 16/11/1.
 */

public class ActivityElement implements Element<Matcher<View>> {
    private String id;
    private String text;
    private String hint;
    private Element<Matcher<View>>[] children;
    private Element<Matcher<View>>[] siblings;
    private Element<Matcher<View>>[] cousinry;
    private Element<Matcher<View>> parent;
    private Element<Matcher<View>> uncle;
    private Element<Matcher<View>> descendant;
    private Element<Matcher<View>> ancestor;
    private int index = -1;
    private Class<? extends View> assignableClass;
    private Matcher[] matchers;
    private boolean dis = true;
    private String content_desc;
    private ActivityElement self;

    String getId() {
        return id;
    }

    String getText() {
        return text;
    }

    String getHint() {
        return hint;
    }

    Element<Matcher<View>>[] getChildren() {
        return children;
    }

    Element<Matcher<View>>[] getSiblings() {
        return siblings;
    }

    Element<Matcher<View>>[] getCousinry() {
        return cousinry;
    }

    Element<Matcher<View>> getParent() {
        return parent;
    }

    Element<Matcher<View>> getUncle() {
        return uncle;
    }

    int getIndex() {
        return index;
    }

    Class<? extends View> getAssignableClass() {
        return assignableClass;
    }

    Matcher[] getMatchers() {
        return matchers;
    }

    boolean isDis() {
        return dis;
    }

    Element<Matcher<View>> getDescendant() {
        return descendant;
    }

    Element<Matcher<View>> getAncestor() {
        return ancestor;
    }

    String getContent_desc() {
        return content_desc;
    }

    ActivityElement getSelf() {
        return self;
    }

    public ActivityElement setSelf(ActivityElement self) {
        this.self = self;
        return this;
    }

    public ActivityElement setDescendant(Element<Matcher<View>> descendant) {
        this.descendant = descendant;
        return this;
    }

    public ActivityElement setAncestor(Element<Matcher<View>> ancestor) {
        this.ancestor = ancestor;
        return this;
    }

    public ActivityElement setId(String id) {
        this.id = id;
        return this;
    }

    public ActivityElement setText(String text) {
        this.text = text;
        return this;
    }

    public ActivityElement setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public ActivityElement setChildren(Element<Matcher<View>>... children) {
        this.children = children;
        return this;
    }

    public ActivityElement setSiblings(Element<Matcher<View>>... siblings) {
        this.siblings = siblings;
        return this;
    }

    public ActivityElement setCousinry(Element<Matcher<View>>... cousinry) {
        this.cousinry = cousinry;
        return this;
    }

    public ActivityElement setParent(Element<Matcher<View>> parent) {
        this.parent = parent;
        return this;
    }

    public ActivityElement setUncle(Element<Matcher<View>> uncle) {
        this.uncle = uncle;
        return this;
    }

    public ActivityElement setIndex(int index) {
        this.index = index;
        return this;
    }

    public ActivityElement setAssignableClass(Class<? extends View> assignableClass) {
        this.assignableClass = assignableClass;
        return this;
    }

    @SafeVarargs
    final public <T> ActivityElement setMatchers(Matcher<? super T>... matchers) {
        this.matchers = matchers;
        return this;
    }

    public ActivityElement setDis(boolean dis) {
        this.dis = dis;
        return this;
    }

    public ActivityElement setContent_desc(String content_desc) {
        this.content_desc = content_desc;
        return this;
    }

    @Override
    public Matcher<View> way() {
        ElementHandle elementHandle = new ElementHandle();
        return elementHandle.all(elementHandle.element2matcher(this));
    }

    @Override
    public AdViewInteraction goal() {
        return given(this.way());
    }


    @Override
    public String toString() {
        String string = "{";
        if( notEmpty(id)) string += "id='" + id + "', ";
        if( notEmpty(text)) string += "text='" + text + "', ";
        if( notEmpty(hint)) string += "hint='" + hint + "', ";
        if( notEmpty(content_desc)) string += "content_desc='" + content_desc + "', ";
        if( notEmpty(children)) string += "children=" + Arrays.toString(children) + ", ";
        if( notEmpty(siblings)) string +=  "siblings=" + Arrays.toString(siblings) + ", ";
        if( notEmpty(cousinry)) string += "cousinry=" + Arrays.toString(cousinry) + ", ";
        if( notEmpty(parent)) string += "parent=" + parent + ", ";
        if( notEmpty(uncle)) string += "uncle=" + uncle + ", ";
        if( notEmpty(ancestor)) string += "ancestor=" + ancestor + ", ";
        if( notEmpty(descendant)) string += "descendant=" + descendant + ", ";
        if(index != -1) string += "index=" + index + ", ";
        if( notEmpty(assignableClass)) string += "assignableClass=" + assignableClass + ", ";
        if( notEmpty(matchers)) string += "matchers=" + Arrays.toString(matchers) + ", " ;
        if(notEmpty(self))  string += "self=" + self.toString() + ",";

        string += "displayed=" + dis + "}";
        return string;
    }


}
