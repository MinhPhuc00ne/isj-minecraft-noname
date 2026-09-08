package com.yourname.darkgathering.client.renderer;

import com.yourname.darkgathering.DarkGatheringMod;
import com.yourname.darkgathering.entity.EvilSpiritEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EvilSpiritRenderer extends HumanoidMobRenderer<EvilSpiritEntity, HumanoidModel<EvilSpiritEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(DarkGatheringMod.MOD_ID, "textures/entity/evil_spirit.png");

    public EvilSpiritRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(EvilSpiritEntity entity) {
        return TEXTURE;
    }
}
