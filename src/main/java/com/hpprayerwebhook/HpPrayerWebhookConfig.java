package com.hpprayerwebhook;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("HpPrayerWebhookConfig")
public interface HpPrayerWebhookConfig extends Config
{
    @ConfigItem(
            keyName = "hpWebhookUrl",
            name = "Webhook for HP",
            description = "Webhook endpoint to send HP notifications"
    )
    default String hpWebhookUrl()
    {
        return "";
    }

    @ConfigItem(
            keyName = "prayerWebhookUrl",
            name = "Webhook for Prayer",
            description = "Webhook endpoint to send Prayer notifications"
    )
    default String prayerWebhookUrl()
    {
        return "";
    }

    @ConfigItem(
            keyName = "hpThreshold",
            name = "HP Threshold",
            description = "Trigger webhook when HP is below or equal to this number"
    )
    default int hpThreshold()
    {
        return 10;
    }

    @ConfigItem(
            keyName = "prayerThreshold",
            name = "Prayer Threshold",
            description = "Trigger webhook when Prayer is below or equal to this number"
    )
    default int prayerThreshold()
    {
        return 10;
    }
}