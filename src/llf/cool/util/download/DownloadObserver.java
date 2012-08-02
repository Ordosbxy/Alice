package llf.cool.util.download;

/**
 * 
 * 观察者模式：定义对象间一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知自动更新
 * @author Administrator
 *
 */
public interface DownloadObserver {
	
	public void onDownloadChanged(DownloadManager manager);
}
