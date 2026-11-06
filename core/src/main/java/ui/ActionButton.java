package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import static ui.TextureUtils.createRoundedTexture;

public class ActionButton extends Table {
    private static final float BUTTON_WIDTH = 165;
    private static final float BUTTON_HEIGHT = 65;
    private static final float ICON_SIZE = 38;
    private final Texture background;

    public ActionButton(String label, Texture icon, Color backgroundColor, Runnable action) {

        background = createRoundedTexture(backgroundColor, 256, 128, 10);

        setBackground(new TextureRegionDrawable(new TextureRegion(background)));

        if (icon != null) {
            Image iconImage = new Image(icon);
            iconImage.setScaling(Scaling.fit);
            add(iconImage).size(38, 38).padLeft(12).padRight(10);
        }

        Label buttonLabel = new Label(label,
            new Label.LabelStyle(ActionButtonResources.getFont(), Color.BLACK));

        buttonLabel.setAlignment(Align.center);

        add(buttonLabel).expandX().center().padRight(15);

        setSize(165, 65);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (action != null) action.run();
            }
        });
    }

    public void dispose() {
        if (background != null) {
            background.dispose();
        }
    }
}
