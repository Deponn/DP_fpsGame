package dp_fpsgame.dp_fpsgame;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleTask extends BukkitRunnable {

    private final Player player;
    private final int maxCount;
    private final Particle particle;
    private int counter;

    public ParticleTask(Player player,int maxCount,Particle particle){
        this.player = player;
        this.particle = particle;
        this.maxCount = maxCount;
        this.counter = 0;
    }

    @Override
    public void run(){
        player.getWorld().spawnParticle(particle,player.getLocation(),10);
        counter += 1;
        if(counter >= maxCount){
            cancel();
        }
    }
}
