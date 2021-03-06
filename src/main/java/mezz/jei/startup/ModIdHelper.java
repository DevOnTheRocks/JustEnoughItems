package mezz.jei.startup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.config.Config;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.lang3.text.WordUtils;

public class ModIdHelper {

	private final Map<String, String> modNamesForIds = new HashMap<String, String>();

	public ModIdHelper() {
		Map<String, ModContainer> modMap = Loader.instance().getIndexedModList();
		for (Map.Entry<String, ModContainer> modEntry : modMap.entrySet()) {
			String lowercaseId = modEntry.getKey().toLowerCase(Locale.ENGLISH);
			String modName = modEntry.getValue().getName();
			modNamesForIds.put(lowercaseId, modName);
		}
	}

	public String getModNameForModId(String modId) {
		String lowercaseModId = modId.toLowerCase(Locale.ENGLISH);
		String modName = modNamesForIds.get(lowercaseModId);
		if (modName == null) {
			modName = WordUtils.capitalize(modId);
			modNamesForIds.put(lowercaseModId, modName);
		}
		return modName;
	}

	public <T> String getModNameForIngredient(T ingredient, IIngredientHelper<T> ingredientHelper) {
		String modId = ingredientHelper.getModId(ingredient);
		return getModNameForModId(modId);
	}

	public <T> List<String> addModNameToIngredientTooltip(List<String> tooltip, T ingredient, IIngredientHelper<T> ingredientHelper) {
		String modNameFormat = Config.getModNameFormat();
		if (modNameFormat.isEmpty()) {
			return tooltip;
		}

		String modName = getModNameForIngredient(ingredient, ingredientHelper);
		if (tooltip.size() > 1) {
			String lastTooltipLine = tooltip.get(tooltip.size() - 1);
			lastTooltipLine = TextFormatting.getTextWithoutFormattingCodes(lastTooltipLine);
			if (modName.equals(lastTooltipLine)) {
				return tooltip;
			}
		}

		List<String> tooltipCopy = new ArrayList<String>(tooltip);
		tooltipCopy.add(modNameFormat + modName);
		return tooltipCopy;
	}
}
