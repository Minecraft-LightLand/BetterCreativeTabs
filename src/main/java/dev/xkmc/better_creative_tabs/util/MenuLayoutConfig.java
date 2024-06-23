
package dev.xkmc.better_creative_tabs.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashMap;

public class MenuLayoutConfig {
	public ResourceLocation id;
	public int height;
	public HashMap<String, Rect> side = new HashMap<>();
	public HashMap<String, Rect> comp = new HashMap<>();

	public MenuLayoutConfig() {
	}

	public ResourceLocation getTexture() {
		return new ResourceLocation(id.getNamespace(), "textures/gui/container/" + id.getPath() + ".png");
	}

	public Rect getComp(String key) {
		return (Rect) this.comp.getOrDefault(key, MenuLayoutConfig.Rect.ZERO);
	}

	public int getHeight() {
		return this.height;
	}

	public int getPlInvX() {
		return 8;
	}

	public int getPlInvY() {
		return this.height - 82;
	}

	@OnlyIn(Dist.CLIENT)
	public ScreenRenderer getRenderer(AbstractContainerScreen<?> gui) {
		return new ScreenRenderer(gui);
	}

	@OnlyIn(Dist.CLIENT)
	public ScreenRenderer getRenderer(Screen gui, int x, int y, int w, int h) {
		return new ScreenRenderer(gui, x, y, w, h);
	}

	public Rect getSide(String key) {
		return (Rect) this.side.getOrDefault(key, MenuLayoutConfig.Rect.ZERO);
	}

	public <T extends Slot> void getSlot(String key, SlotFactory<T> fac, SlotAcceptor con) {
		Rect c = this.getComp(key);

		for (int j = 0; j < c.ry; ++j) {
			for (int i = 0; i < c.rx; ++i) {
				T slot = fac.getSlot(c.x + i * c.w, c.y + j * c.h);
				if (slot != null) {
					con.addSlot(key, i, j, slot);
				}
			}
		}

	}

	public int getWidth() {
		return 176;
	}

	public boolean within(String key, double x, double y) {
		Rect c = this.getComp(key);
		return x > (double) c.x && x < (double) (c.x + c.w) && y > (double) c.y && y < (double) (c.y + c.h);
	}

	public static class Rect {
		public static final Rect ZERO = new Rect();
		public int x;
		public int y;
		public int w;
		public int h;
		public int rx = 1;
		public int ry = 1;

		public Rect() {
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class ScreenRenderer {
		private final int x;
		private final int y;
		private final int w;
		private final int h;
		private final Screen scr;

		public ScreenRenderer(Screen gui, int x, int y, int w, int h) {
			this.scr = gui;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		private ScreenRenderer(AbstractContainerScreen<?> scrIn) {
			this.x = scrIn.getGuiLeft();
			this.y = scrIn.getGuiTop();
			this.w = scrIn.getXSize();
			this.h = scrIn.getYSize();
			this.scr = scrIn;
		}

		public void draw(GuiGraphics g, String c, String s) {
			Rect cr = MenuLayoutConfig.this.getComp(c);
			Rect sr = MenuLayoutConfig.this.getSide(s);
			g.blit(MenuLayoutConfig.this.getTexture(), this.x + cr.x, this.y + cr.y, sr.x, sr.y, sr.w, sr.h);
		}

		public void draw(GuiGraphics g, String c, String s, int xoff, int yoff) {
			Rect cr = MenuLayoutConfig.this.getComp(c);
			Rect sr = MenuLayoutConfig.this.getSide(s);
			g.blit(MenuLayoutConfig.this.getTexture(), this.x + cr.x + xoff, this.y + cr.y + yoff, sr.x, sr.y, sr.w, sr.h);
		}

		public void drawBottomUp(GuiGraphics g, String c, String s, int prog, int max) {
			if (prog != 0 && max != 0) {
				Rect cr = MenuLayoutConfig.this.getComp(c);
				Rect sr = MenuLayoutConfig.this.getSide(s);
				int dh = sr.h * prog / max;
				g.blit(MenuLayoutConfig.this.getTexture(), this.x + cr.x, this.y + cr.y + sr.h - dh, sr.x, sr.y + sr.h - dh, sr.w, dh);
			}
		}

		public void drawLeftRight(GuiGraphics g, String c, String s, int prog, int max) {
			if (prog != 0 && max != 0) {
				Rect cr = MenuLayoutConfig.this.getComp(c);
				Rect sr = MenuLayoutConfig.this.getSide(s);
				int dw = sr.w * prog / max;
				g.blit(MenuLayoutConfig.this.getTexture(), this.x + cr.x, this.y + cr.y, sr.x, sr.y, dw, sr.h);
			}
		}

		public void drawLiquid(GuiGraphics g, String c, double per, int height, int sw, int sh) {
			Rect cr = MenuLayoutConfig.this.getComp(c);
			int base = cr.y + height;
			int h = (int) Math.round(per * (double) height);
			this.circularBlit(g, this.x + cr.x, base - h, 0, -h, cr.w, h, sw, sh);
		}

		public void start(GuiGraphics g) {
			this.scr.renderBackground(g);
			g.blit(MenuLayoutConfig.this.getTexture(), this.x, this.y, 0, 0, this.w, this.h);
		}

		private void circularBlit(GuiGraphics g, int sx, int sy, int ix, int iy, int w, int h, int iw, int ih) {
			int x0 = ix;
			int yb = iy;
			int x1 = w;

			int x2;
			for (x2 = sx; x0 < 0; x0 += iw) {
			}

			while (yb < ih) {
				yb += ih;
			}

			while (x1 > 0) {
				int dx = Math.min(x1, iw - x0);
				int y0 = yb;
				int y1 = h;

				int dy;
				for (int y2 = sy; y1 > 0; y2 += dy) {
					dy = Math.min(y1, ih - y0);
					g.blit(MenuLayoutConfig.this.getTexture(), x2, y2, x0, y0, x1, y1);
					y1 -= dy;
					y0 += dy;
				}

				x1 -= dx;
				x0 += dx;
				x2 += dx;
			}

		}
	}

	public interface SlotFactory<T extends Slot> {
		@Nullable
		T getSlot(int var1, int var2);
	}

	public interface SlotAcceptor {
		void addSlot(String var1, int var2, int var3, Slot var4);
	}
}
