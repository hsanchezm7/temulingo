module es.um.pds.learningapp {
    requires javafx.controls;
    requires javafx.graphics;
    exports es.um.pds.learningapp;

    exports es.um.pds.learningapp.window to javafx.graphics;
}