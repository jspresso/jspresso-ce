package ${package}.startup.qooxdoo;

import java.util.List;

import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.qooxdoo.rpc.Remote;
import org.jspresso.framework.qooxdoo.rpc.RemoteException;

import ${package}.startup.remote.RemoteApplicationStartup;

/**
 * Qooxdoo application startup class.
 */
public class QooxdooApplicationStartup extends RemoteApplicationStartup
    implements Remote {

  /**
   * Delegates to start.
   * 
   * @param startupLanguage
   *          the client language.
   * @param timezoneOffset
   *          the client timezone offset in milliseconds.
   * @return the commands to be executed by the client peer on startup.
   * @throws RemoteException
   *           whenever an exception occurs.
   */
  public List<RemoteCommand> startQx(String startupLanguage, int timezoneOffset)
      throws RemoteException {
    return super.start(startupLanguage, timezoneOffset);
  }

  /**
   * Recieves and handle a list of commands.
   * 
   * @param commands
   *          the command list.
   * @return the resulting commands.
   * @throws RemoteException
   *           whenever an exception occurs.
   */
  public List<RemoteCommand> handleCommandsQx(List<RemoteCommand> commands)
      throws RemoteException {
    return handleCommands(commands);
  }
}
