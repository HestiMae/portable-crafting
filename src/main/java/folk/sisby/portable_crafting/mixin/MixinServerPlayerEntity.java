package folk.sisby.portable_crafting.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import folk.sisby.portable_crafting.PortableCrafting;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;canUse(Lnet/minecraft/entity/player/PlayerEntity;)Z"))
	private boolean applyCanUse(boolean original)
	{
		return original || PortableCrafting.canUse((ServerPlayerEntity) (Object) this);
	}
}
