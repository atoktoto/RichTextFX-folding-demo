module outlinerfx.main {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.fxmisc.richtext;
    requires reactfx;
    requires kotlin.stdlib;
    requires annotations;

    requires undofx;
    requires kotlinx.coroutines.core.jvm;
    requires java.xml.bind;


    exports richtext.demo.folding;
}