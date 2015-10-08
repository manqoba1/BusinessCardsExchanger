package codetribe.sifiso.com.bcelibrary.Models;

/**
 * Created by sifiso on 2015-09-24.
 */
public class DrawerItem {
    public int iconId;
    public String title;
    public int count;

    public DrawerItem(int iconId, String title) {
        this.iconId = iconId;
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
