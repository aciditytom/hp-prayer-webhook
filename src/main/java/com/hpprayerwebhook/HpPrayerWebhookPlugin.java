package com.hpprayerwebhook;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@PluginDescriptor(
	name = "Webhooks for HP and Prayer"
)
public class HpPrayerWebhookPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private HpPrayerWebhookConfig config;

    private boolean hpTriggered = false;
    private boolean prayerTriggered = false;

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        if (client.getLocalPlayer() == null)
        {
            return;
        }

        int currentHp = client.getBoostedSkillLevel(Skill.HITPOINTS);
        int currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);

        // --- HP Logic ---
        if (currentHp <= config.hpThreshold() && !hpTriggered)
        {
            sendWebhook(config.hpWebhookUrl(), "HP is low! Current HP: " + currentHp);
            hpTriggered = true;
        }
        else if (currentHp > config.hpThreshold() && hpTriggered)
        {
            hpTriggered = false;
        }

        // --- Prayer Logic ---
        if (currentPrayer <= config.prayerThreshold() && !prayerTriggered)
        {
            sendWebhook(config.prayerWebhookUrl(), "Prayer is low! Current Prayer: " + currentPrayer);
            prayerTriggered = true;
        }
        else if (currentPrayer > config.prayerThreshold() && prayerTriggered)
        {
            prayerTriggered = false;
        }
    }

    private void sendWebhook(String webhookUrl, String message)
    {
        try
        {
            if (webhookUrl == null || webhookUrl.isEmpty())
            {
                return;
            }

            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = "{\"content\": \"" + message.replace("\"", "\\\"") + "\"}";
            try (OutputStream os = conn.getOutputStream())
            {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Webhook sent (HTTP " + responseCode + ")");
            conn.disconnect();
        }
        catch (Exception e)
        {
            System.err.println("Error sending webhook: " + e.getMessage());
        }
    }

    @Provides
    HpPrayerWebhookConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(HpPrayerWebhookConfig.class);
    }
}

