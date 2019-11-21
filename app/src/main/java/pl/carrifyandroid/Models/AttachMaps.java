package pl.carrifyandroid.Models;

public class AttachMaps {
    private boolean onAttach;

    public AttachMaps(boolean onAttach) {
        this.onAttach = onAttach;
    }

    public boolean isOnAttach() {
        return onAttach;
    }

    public void setOnAttach(boolean onAttach) {
        this.onAttach = onAttach;
    }
}
