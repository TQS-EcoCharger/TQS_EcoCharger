import React from 'react';
import styles from '../css/ChargingRing.module.css';

export default function ChargingRingWithFill({ batteryPercentage = 56.27 }) {
  const bubbleCount = 0;
  const batteryPercentage2 = batteryPercentage - 2;

  const bubbles = Array.from({ length: bubbleCount }, (_, i) => ({
    id: i,
    size: Math.random() * 8 + 6,
    left: 5 + Math.random() * 90,
    bottom: -2,
    delay: Math.random() * 3,
    duration: 3 + Math.random() * 3,
  }));

  return (
    <div className={styles.container}>
      <div className={styles.circle}>
        {/* Fill background */}
        <div
          className={styles.fill}
          style={{ height: `${batteryPercentage}%` }}
        />

        {/* Animated Wave on top of fill */}
        <svg
          className={styles.wave}
          viewBox="0 0 100 20"
          preserveAspectRatio="none"
          style={{ bottom: `${batteryPercentage}%` }}
        >
          <path d="M0 10 Q 25 0 50 10 T 100 10 V20 H0 Z" />
        </svg>

        {/* Battery Label */}
        <div className={styles.label}>
          {batteryPercentage}%
        </div>

        {/* Floating Bubbles */}
        {bubbles.map(b => (
          <div
            key={b.id}
            className={styles.bubble}
            style={{
              width: `${b.size}px`,
              height: `${b.size}px`,
              left: `${b.left}%`,
              bottom: `${batteryPercentage2}%`,
              animationDelay: `${b.delay}s`,
              animationDuration: `${b.duration}s, ${b.duration}s`,
            }}
          />
        ))}
      </div>
    </div>
  );
}
