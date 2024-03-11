package com.if_connect.request.requestbody;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Page<T> {
    @SerializedName("content")
    private List<T> content;
//    private Pageable pageable;
    @SerializedName("last")
    private boolean last;
    @SerializedName("totalPages")
    private int totalPages;
    @SerializedName("totalElements")
    private int totalElements;
    @SerializedName("size")
    private int size;
    @SerializedName("number")
    private int number;
//    private Sort sort;
    @SerializedName("first")
    private boolean first;
    @SerializedName("numberOfElements")
    private int numberOfElements;
    @SerializedName("empty")
    private boolean empty;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}


//class Pageable{
//    private int pageNumber;
//    private int pageSize;
//    private Sort sort;
//    private int offset;
//    private boolean paged;
//    private boolean unpaged;
//}
//
//class Sort{
//    private boolean empty;
//    private boolean sorted;
//    private boolean unsorted;
//}