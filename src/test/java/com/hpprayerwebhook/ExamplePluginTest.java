package com.hpprayerwebhook;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HpPrayerWebhookPlugin.class);
		RuneLite.main(args);
	}
}