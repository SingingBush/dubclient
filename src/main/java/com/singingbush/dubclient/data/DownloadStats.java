package com.singingbush.dubclient.data;

import java.util.Objects;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class DownloadStats {

    private Integer total;
    private Integer monthly;
    private Integer weekly;
    private Integer daily;

    public DownloadStats(Integer total, Integer monthly, Integer weekly, Integer daily) {
        this.total = total;
        this.monthly = monthly;
        this.weekly = weekly;
        this.daily = daily;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getMonthly() {
        return monthly;
    }

    public Integer getWeekly() {
        return weekly;
    }

    public Integer getDaily() {
        return daily;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadStats that = (DownloadStats) o;
        return Objects.equals(total, that.total) &&
            Objects.equals(monthly, that.monthly) &&
            Objects.equals(weekly, that.weekly) &&
            Objects.equals(daily, that.daily);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, monthly, weekly, daily);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DownloadStats{");
        sb.append("total=").append(total);
        sb.append(", monthly=").append(monthly);
        sb.append(", weekly=").append(weekly);
        sb.append(", daily=").append(daily);
        sb.append('}');
        return sb.toString();
    }
}
