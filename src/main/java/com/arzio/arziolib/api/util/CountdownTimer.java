package com.arzio.arziolib.api.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CountdownTimer{

	private static final long SECONDS_PER_TICK = 1;
	public static final long SECOND = 1L;
	public static final long MINUTE = SECOND * 60L;
	public static final long HOUR = MINUTE * 60L;
	public static final long DAY = HOUR * 24L;
	
	private Plugin plugin;
	private final long initialDurationSeconds;
	private TimeCallback callback;
	private TickDelay tickDelay;
	
	private long accumulatedSeconds;
	private long passedSeconds;
	private long remainingSeconds;
	private BukkitTask task;
	
	public CountdownTimer(Plugin plugin, long durationSeconds, TickDelay updateDelay, TickTimeCallback updateCallback) {
		this(plugin, durationSeconds, updateCallback);
		this.tickDelay = updateDelay;
	}
	
	public CountdownTimer(Plugin plugin, long durationSeconds, TimeCallback timeCallback) {
		this.plugin = plugin;
		this.remainingSeconds = durationSeconds;
		this.callback = timeCallback;
		this.initialDurationSeconds = durationSeconds;
	}
	
	public void start() {
		this.cancel();
		
		callback.onStart(initialDurationSeconds * 1000L);
		
		this.task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			
			@Override
			public void run() {
				remainingSeconds -= SECONDS_PER_TICK;
				passedSeconds = initialDurationSeconds - remainingSeconds;
				
				if (callback instanceof TickTimeCallback) {
					TickTimeCallback updatable = (TickTimeCallback) callback;
					
					accumulatedSeconds += SECONDS_PER_TICK;
					if (tickDelay.isHigherOrEqualToSeconds(accumulatedSeconds)) {
						updatable.onTick(remainingSeconds * 1000L, passedSeconds * 1000L);
						accumulatedSeconds = 0L;
					}
				}
				
				if (remainingSeconds <= 0) {
					forceEnd();
				}
			}
			
		}, SECONDS_PER_TICK * 20L, SECONDS_PER_TICK * 20L);
	}
	
	public void cancel() {
		if (this.task != null)
			this.task.cancel();
	}
	
	public void forceEnd() {
		this.remainingSeconds = 0L;
		callback.onEnd();
		cancel();
	}
	
	public long getRemainingSeconds() {
		return this.remainingSeconds;
	}
	
	public long getPassedSeconds() {
		return this.passedSeconds;
	}
	
	public TickDelay getUpdateDelay() {
		return this.tickDelay;
	}
	
	public static interface TimeCallback {
		public void onStart(long durationMillis);
		public void onEnd();
	}
	
	public static interface TickTimeCallback extends TimeCallback {
		public void onTick(long remainingMillis, long passedMillis);
	}
	
	public static class TickDelay {
		
		public static final TickDelay SECOND = new TickDelay(CountdownTimer.SECOND);
		public static final TickDelay MINUTE = new TickDelay(CountdownTimer.MINUTE);
		public static final TickDelay HOUR = new TickDelay(CountdownTimer.HOUR);
		public static final TickDelay DAY = new TickDelay(CountdownTimer.DAY);
		
		private long secondsAmount;
		
		private TickDelay(long secondsAmount){
			this.secondsAmount = secondsAmount;
		}
		
		public boolean isHigherOrEqualToSeconds(long seconds) {
			return this.secondsAmount >= seconds;
		}
		
		public long getSecondsAmount() {
			return this.secondsAmount;
		}
		
		public static TickDelay custom(int amount, TickDelay delay) {
			return new TickDelay(delay.getSecondsAmount() * (long) amount);
		}
	}
}
