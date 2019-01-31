package com.nc.airport.backend.persistence.eav.repository;

import lombok.extern.log4j.Log4j2;

/**
 * Page is zero-based. If page is less than 0 it is set to 0<br>
 * Size cannot be 0. If size is 0 it is set to 10.<br>
 * Default size is 10.
 */
@Log4j2
public class Page {
    private int size;
    private int page;
    private int offset;

    /**
     * Page is zero-based. If page is less than 0 it is set to 0<br>
     * Size cannot be 0. If size is 0 it is set to 10.<br>
     */
    public Page(int size, int page) {
        if (size < 1) {
            log.warn("Tried to set page size less than 1 (" + size + "). Set to 10.");
            size = 10;
        }
        if (page < 0) {
            log.warn("Tried to set page index less than 0 (" + page + "). Set to 0.");
            page = 0;
        }
        this.size = size;
        this.page = page;
        offset = page * size;
    }

    /**
     * Page is zero-based. If page is less than 0 it is set to 0<br>
     * Default size is 10.
     */
    public Page(int page) {
        this(10, page);
    }

    public int getFirstRow() {
        return 1 + offset;
    }

    public int getLastRow() {
        return size + offset;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public Page next() {
        page++;
        return this;
    }

    public Page prev() {
        if (page == 0) {
            log.warn("Tried to access prev page which is -1. Set to 0");
            return this;
        }
        else {
            page--;
            return this;
        }
    }
}
