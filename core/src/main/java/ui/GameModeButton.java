package ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class GameModeButton extends Table {
    public GameModeButton(String title, Texture icon, BitmapFont font, Drawable background, Runnable onClick) {
        setBackground(background);

        // Mode icon
        Image image = new Image(icon);
        image.setScaling(Scaling.fit);

        // Mode title
        Label.LabelStyle labelStyle =
            new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.BLACK);

        Label label = new Label(title, labelStyle);
        label.setAlignment(com.badlogic.gdx.utils.Align.center);

        // Vertical layout
        add(image).size(150, 165).padTop(15);

        row();

        add(label).width(200).padTop(12).padBottom(18);

        // Card dimensions
        setSize(220, 280);

        // Click listener
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClick != null) { onClick.run(); }
            }
        });
    }
}
