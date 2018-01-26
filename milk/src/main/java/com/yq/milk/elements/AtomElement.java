package com.yq.milk.elements;

import android.support.test.espresso.web.model.Atom;

import com.alibaba.fastjson.JSON;

import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.selectFrameByIdOrName;
import static android.support.test.espresso.web.webdriver.DriverAtoms.selectFrameByIndex;
import static android.support.test.espresso.web.webdriver.Locator.CLASS_NAME;
import static android.support.test.espresso.web.webdriver.Locator.CSS_SELECTOR;
import static android.support.test.espresso.web.webdriver.Locator.ID;
import static android.support.test.espresso.web.webdriver.Locator.LINK_TEXT;
import static android.support.test.espresso.web.webdriver.Locator.NAME;
import static android.support.test.espresso.web.webdriver.Locator.PARTIAL_LINK_TEXT;
import static android.support.test.espresso.web.webdriver.Locator.TAG_NAME;
import static android.support.test.espresso.web.webdriver.Locator.XPATH;
import static com.yq.milk.utils.DoIt.notEmpty;

/**
 * Created by king on 16/11/8.
 */

public class AtomElement implements Element<Atom> {

    private String id;
    private String css;
    private String className;
    private String linkText;
    private String name;
    private String partialLinkText;
    private String tagName;
    private String xpath;
    private String frameIdOrName;
    private int frameIndex = -1;

    public AtomElement setId(String id) {
        this.id = id;
        return this;
    }

    public AtomElement setCss(String css) {
        this.css = css;
        return this;
    }

    public AtomElement setClassName(String className) {
        this.className = className;
        return this;
    }

    public AtomElement setLinkText(String linkText) {
        this.linkText = linkText;
        return this;
    }

    public AtomElement setName(String name) {
        this.name = name;
        return this;
    }

    public AtomElement setPartialLinkText(String partialLinkText) {
        this.partialLinkText = partialLinkText;
        return this;
    }

    public AtomElement setTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public AtomElement setXpath(String xpath) {
        this.xpath = xpath;
        return this;
    }

    public AtomElement setFrameIdOrName(String frameIdOrName) {
        this.frameIdOrName = frameIdOrName;
        return this;
    }

    public AtomElement setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
        return this;
    }

    @Override
    public Atom way() {
        if(notEmpty(id))                return findElement(ID , id);
        if(notEmpty(css))               return findElement(CSS_SELECTOR ,css);
        if(notEmpty(className))        return findElement(CLASS_NAME ,className);
        if(notEmpty(linkText))         return findElement(LINK_TEXT , linkText);
        if(notEmpty(name))              return findElement(NAME ,name);
        if(notEmpty(partialLinkText))  return findElement(PARTIAL_LINK_TEXT ,partialLinkText);
        if(notEmpty(tagName))           return findElement(TAG_NAME ,tagName);
        if(notEmpty(xpath))             return findElement(XPATH ,xpath);
        if(notEmpty(frameIdOrName))    return selectFrameByIdOrName(frameIdOrName);
        if(frameIndex != -1)            return selectFrameByIndex(frameIndex);
        return null;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}
