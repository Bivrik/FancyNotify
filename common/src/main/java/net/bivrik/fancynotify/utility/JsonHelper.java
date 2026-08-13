package net.bivrik.fancynotify.utility;

import com.google.gson.*;
import net.bivrik.fancynotify.config.Setting;
import net.bivrik.fancynotify.core.Log;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public final class JsonHelper {
    private JsonHelper() {}

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocationAdapter())
            .registerTypeAdapter(Setting.class, new SettingAdapter())
            .create();

    private static class ResourceLocationAdapter implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {
        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return ResourceLocation.parse(json.getAsString());
        }

        @Override
        public JsonElement serialize(ResourceLocation source, Type typeOfSource, JsonSerializationContext context) {
            return new JsonPrimitive(source.toString());
        }
    }

    private static class SettingAdapter implements JsonSerializer<Setting<?>>, JsonDeserializer<Setting<?>> {
        @Override
        public Setting<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Type valueType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
            Object value = context.deserialize(new JsonPrimitive(json.getAsString()), valueType);
            return new Setting<>(value);
        }

        @Override
        public JsonElement serialize(Setting<?> source, Type sourceType, JsonSerializationContext context) {
            return context.serialize(source.get());
        }
    }

    public static <T> Optional<T> tryToRead(File jsonFile, Class<T> classReference) {
        try (FileReader reader = new FileReader(jsonFile)) {
            T data = GSON.fromJson(reader, classReference);
            return Optional.of(data);
        } catch (Exception e) {
            Log.error("Could not read json file {}: {}", jsonFile.getName(), e.getMessage());
            return Optional.empty();
        }
    }

    public static boolean tryToWrite(File jsonFile, Object data) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            GSON.toJson(data, writer);
            return true;
        } catch (Exception e) {
            Log.error("Could not write json file {}: {}", jsonFile.getName(), e.getMessage());
            return false;
        }
    }
}
