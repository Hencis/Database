package org.example;

public class Accommodation {
    private Integer id;
    private String type;
    private String bedType;
    private Integer maxGuests;
    private String description;

    public Accommodation(Integer id, String type, String bedType, Integer maxGuests, String description) {
        this.id = id;
        this.type = type;
        this.bedType = bedType;
        this.maxGuests = maxGuests;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
