package io.github.vulpesstella.retrolytra.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.vulpesstella.retrolytra.item.ItemElytra;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.ForgeSubscribe;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ElytraRenderHandler {

    private static final ResourceLocation WING_TEXTURE =
            new ResourceLocation("retrolytra:textures/entity/elytra.png");
    private static final ResourceLocation GLINT_TEXTURE =
            new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private final ModelElytra model = new ModelElytra();

    @ForgeSubscribe
    public void onSpecialsPre(RenderPlayerEvent.Specials.Pre event) {
        if (ItemElytra.getElytra(event.entityPlayer) != null) {
            event.renderCape = false;
        }
    }

    @ForgeSubscribe
    public void onSpecialsPost(RenderPlayerEvent.Specials.Post event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack chest = ItemElytra.getElytra(player);
        if (chest == null) {
            return;
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(this.getWingTexture(player));
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 0.125F);

        int dye = ItemElytra.getVisibleDyeColor(chest);
        if (dye >= 0 && dye < ItemDye.dyeColors.length && dye != 15) {
            int rgb = ItemDye.dyeColors[dye];
            GL11.glColor3f(
                    (float) (rgb >> 16 & 255) / 255.0F,
                    (float) (rgb >> 8 & 255) / 255.0F,
                    (float) (rgb & 255) / 255.0F);
        } else {
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
        }

        this.model.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        this.model.render(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

        if (chest.isItemEnchanted()) {
            this.renderGlint(player, event.partialRenderTick);
        }

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    /**
     * 1.9 uses the player's cape texture for the elytra when a cape is visible.
     *
     * <p>In stock 1.6.4 this branch is normally inert: skins and capes are
     * fetched from the retired {@code http://skins.minecraft.net} service, so
     * the cape texture never finishes uploading. It only activates if a
     * skin/cape backport or launcher has already supplied the cape.</p>
     */
    private ResourceLocation getWingTexture(EntityPlayer player) {
        if (player instanceof AbstractClientPlayer) {
            AbstractClientPlayer client = (AbstractClientPlayer) player;
            if (client.getTextureCape() != null
                    && client.getTextureCape().isTextureUploaded()
                    && client.getLocationCape() != null) {
                return client.getLocationCape();
            }
        }
        return WING_TEXTURE;
    }

    private void renderGlint(EntityPlayer player, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(GLINT_TEXTURE);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthFunc(GL11.GL_EQUAL);
        GL11.glDepthMask(false);

        float time = player.ticksExisted + partialTicks;
        for (int pass = 0; pass < 2; pass++) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            float scale = 0.76F;
            GL11.glColor4f(0.5F * scale, 0.25F * scale, 0.8F * scale, 1.0F);

            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();
            float textureScale = 0.33333334F;
            GL11.glScalef(textureScale, textureScale, textureScale);
            GL11.glRotatef(30.0F - pass * 60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.0F, time * (0.001F + pass * 0.003F) * 20.0F, 0.0F);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            this.model.render(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        }

        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopAttrib();
    }
}

