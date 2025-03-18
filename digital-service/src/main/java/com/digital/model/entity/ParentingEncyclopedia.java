package com.digital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName parenting_encyclopedia
 */
@TableName(value ="parenting_encyclopedia")
@Data
public class ParentingEncyclopedia {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 0:备孕期, 1:孕产期管理, 2:产褥期, 3:产后恢复, 4:0-1岁宝宝, 5:1-2岁宝宝, 6:2-3岁宝宝, 7:3-5岁宝宝, 8:5-10岁宝宝, 9:10-15岁宝宝
     */
    private Integer stage;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String content;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ParentingEncyclopedia other = (ParentingEncyclopedia) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStage() == null ? other.getStage() == null : this.getStage().equals(other.getStage()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStage() == null) ? 0 : getStage().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", stage=").append(stage);
        sb.append(", userId=").append(userId);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append("]");
        return sb.toString();
    }
}