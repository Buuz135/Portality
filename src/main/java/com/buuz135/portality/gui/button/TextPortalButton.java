/**
 * MIT License
 *
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buuz135.portality.gui.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.client.gui.addon.BasicButtonAddon;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextPortalButton extends PosButton {

    private final String display;
    private Supplier<Consumer<Screen>> screenConsumer;

    public TextPortalButton(int posX, int posY, int sizeX, int sizeY, String display) {
        super(posX, posY, sizeX, sizeY);
        this.display = display;
        this.screenConsumer = () -> screen -> {
        };
    }

    public String getDisplay() {
        return display;
    }

    public TextPortalButton setClientConsumer(Supplier<Consumer<Screen>> screenConsumer) {
        this.screenConsumer = screenConsumer;
        return this;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Collections.singletonList(() -> new TextButtonAddon(this, display, screenConsumer.get()));
    }

    public class TextButtonAddon extends BasicButtonAddon {

        private String text;
        private Consumer<Screen> supplier;

        public TextButtonAddon(PosButton posButton, String text, Consumer<Screen> supplier) {
            super(posButton);
            this.text = text;
            this.supplier = supplier;
        }

        @Override
        public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
            super.drawGuiContainerBackgroundLayer(screen, provider, guiX, guiY, mouseX, mouseY, partialTicks);
            String string = new TranslationTextComponent(text).getUnformattedComponentText();
            TextFormatting color = isInside(screen, mouseX - guiX, mouseY - guiY) ? TextFormatting.YELLOW : TextFormatting.WHITE;
            Minecraft.getInstance().fontRenderer.drawString(color + string, guiX + this.getPosX() + this.getXSize() / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(string) / 2, guiY + this.getPosY() + this.getYSize() / 2f - 3.5f, 0xFFFFFF);
        }

        @Override
        public void handleClick(Screen tile, int guiX, int guiY, double mouseX, double mouseY, int button) {
            super.handleClick(tile, guiX, guiY, mouseX, mouseY, button);
            supplier.accept(tile);
        }
    }
}