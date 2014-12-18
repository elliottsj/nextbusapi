package com.elliottsj.nextbus.impl.simplexml.messages;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * TODO: Javadoc
 */
@Root
public class Message {

    @Attribute
    private String id;

    @Attribute
    private String creator;

    @Attribute(required = false)
    private double startBoundary;

    @Attribute(required = false)
    private String startBoundaryStr;

    @Attribute(required = false)
    private double endBoundary;

    @Attribute(required = false)
    private String endBoundaryStr;

    @Attribute(required = false)
    private boolean sendToBuses;

    @Element
    private String text;

    @Element(required = false)
    private String textSecondaryLanguage;

    @Element(required = false)
    private String phonemeText;

    @Element
    private String smsText;

    @Element(required = false)
    private int priority;

    @ElementList(inline = true, required = false)
    private List<RouteConfiguredForMessage> routesConfiguredForMessage;

    @ElementList(inline = true, required = false)
    private List<Interval> intervals;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public double getStartBoundary() {
        return startBoundary;
    }

    public void setStartBoundary(double startBoundary) {
        this.startBoundary = startBoundary;
    }

    public String getStartBoundaryStr() {
        return startBoundaryStr;
    }

    public void setStartBoundaryStr(String startBoundaryStr) {
        this.startBoundaryStr = startBoundaryStr;
    }

    public double getEndBoundary() {
        return endBoundary;
    }

    public void setEndBoundary(double endBoundary) {
        this.endBoundary = endBoundary;
    }

    public String getEndBoundaryStr() {
        return endBoundaryStr;
    }

    public void setEndBoundaryStr(String endBoundaryStr) {
        this.endBoundaryStr = endBoundaryStr;
    }

    public boolean isSendToBuses() {
        return sendToBuses;
    }

    public void setSendToBuses(boolean sendToBuses) {
        this.sendToBuses = sendToBuses;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextSecondaryLanguage() {
        return textSecondaryLanguage;
    }

    public void setTextSecondaryLanguage(String textSecondaryLanguage) {
        this.textSecondaryLanguage = textSecondaryLanguage;
    }

    public String getPhonemeText() {
        return phonemeText;
    }

    public void setPhonemeText(String phonemeText) {
        this.phonemeText = phonemeText;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<RouteConfiguredForMessage> getRoutesConfiguredForMessage() {
        return routesConfiguredForMessage;
    }

    public void setRoutesConfiguredForMessage(List<RouteConfiguredForMessage> routesConfiguredForMessage) {
        this.routesConfiguredForMessage = routesConfiguredForMessage;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    /**
     * TODO: Javadoc
     */
    @Root
    public static class RouteConfiguredForMessage {

        @Attribute
        private String tag;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        /**
         * TODO: Javadoc
         */
        @Root
        public static class Stop {

            @Attribute
            private String tag;

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            @Attribute
            private String title;

        }

    }

}
