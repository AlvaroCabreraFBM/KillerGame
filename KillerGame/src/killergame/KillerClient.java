package killergame;

public class KillerClient implements Runnable {

    // Attributes
    private VisualHandler module;
    private KillerPad pad;
    private long refreshInterval;
    
    // Constructror
    public KillerClient(VisualHandler module, long refreshInterval) {
        this.module = module;
        this.refreshInterval = refreshInterval;
    }
    
    public KillerClient(KillerPad pad, long refreshInterval) {
        this.pad = pad;
        this.refreshInterval = refreshInterval;
    }
    
    // Methods get
    public long getRefreshInterval() {
        return this.refreshInterval;
    }
    
    // Methods set
    public void setRefreshInterval(long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }
    
    // Main activity
    @Override
    public void run() {
        
        while (true) {
            
            if (this.module != null && this.module.getHost() != null && !this.module.isConnected()) {
                this.module.makeContact();
            }
            
            if (this.pad != null && this.pad.getHost() != null && !this.pad.isConnected()) {
                this.pad.makeContact();
            }
            
            try {
                Thread.sleep(this.refreshInterval);
            } catch (InterruptedException error) {
                
            }
            
        }
    }

}
